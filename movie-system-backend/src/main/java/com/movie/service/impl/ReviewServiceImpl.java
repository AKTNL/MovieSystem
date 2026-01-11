package com.movie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.movie.common.JwtUtils;
import com.movie.entity.Movie;
import com.movie.entity.Review;
import com.movie.entity.ReviewVo;
import com.movie.entity.User;
import com.movie.mapper.MovieMapper;
import com.movie.mapper.ReviewMapper;
import com.movie.mapper.UserMapper;
import com.movie.service.ReviewService;
import com.movie.websocket.WebSocketServer;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements ReviewService {
    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<ReviewVo> getReviewsByMovieId(Long movieId){
        //1.先查电影信息拿到标题
        Movie movie = movieMapper.selectById(movieId);
        if(movie == null){
            throw new RuntimeException("电影不存在");
        }
        //2.调用Mapper执行存储过程
        List<ReviewVo> list = baseMapper.selectReviewsByMovieTitle(movie.getTitle());

        // 3. 判断当前用户是否点赞
        String token = request.getHeader("token");
        Long currentUserId = null;
        if (token != null && !token.isEmpty()) {
            try {
                // 解析 Token 获取当前用户 ID
                Claims claims = JwtUtils.parseToken(token);
                currentUserId = Long.parseLong(claims.getSubject());
            } catch (Exception e) {
                // Token 无效，视为未登录，不处理
            }
        }

        if (currentUserId != null) {
            // 已登录，遍历列表查 Redis
            for (ReviewVo vo : list) {
                String key = "review:like:" + vo.getReviewId();
                // 查 Redis 看看我点赞没
                Boolean isLiked = redisTemplate.opsForSet().isMember(key, currentUserId.toString());
                vo.setIsLiked(Boolean.TRUE.equals(isLiked));
            }
        } else {
            // 未登录，全是 false
            list.forEach(vo -> vo.setIsLiked(false));
        }

        return list;
    }

    @Override
    @Transactional
    public Integer toggleLike(Long reviewId, Long userId) {
        // Redis Key 格式: review:like:{reviewId}
        String key = "review:like:" + reviewId;
        String userIdStr = userId.toString();

        // 1. 判断用户是否已点赞 (Redis SISMEMBER)
        Boolean isMember = redisTemplate.opsForSet().isMember(key, userIdStr);

        Review review = getById(reviewId);
        if (review == null) throw new RuntimeException("评论不存在");

        // 防止数据库旧数据 likeCount 为 null
        int currentCount = Optional.ofNullable(review.getLikeCount()).orElse(0);

        if (Boolean.TRUE.equals(isMember)) {
            // --- 情况 A：已经点过赞 -> 执行取消点赞 ---

            // 1. Redis 移除
            redisTemplate.opsForSet().remove(key, userIdStr);

            // 2. 评论表总数 -1 (最小为0)
            int newCount = Math.max(0, currentCount - 1);
            review.setLikeCount(newCount);
            updateById(review);

            return newCount;
        } else {
            // --- 情况 B：没点过赞 -> 执行点赞 ---

            // 1. Redis 添加
            redisTemplate.opsForSet().add(key, userIdStr);

            // 2. 评论表总数 +1
            int newCount = currentCount + 1;
            review.setLikeCount(newCount);
            updateById(review);

            // 1. 排除自己给自己点赞 (不发通知)
            if (!userId.equals(review.getUserId())) {
                try {
                    // 2. 查询点赞人的信息
                    User liker = userMapper.selectById(userId);
                    String likerName = (liker != null) ? liker.getNickname() : "有人";

                    // 3. 查询电影信息 (为了告诉用户是哪部电影的评论)
                    Movie movie = movieMapper.selectById(review.getMovieId());
                    String movieTitle = (movie != null) ? movie.getTitle() : "电影";

                    // 4. 拼接消息内容
                    // 格式：用户 张三 点赞了你在《肖申克的救赎》下的评论
                    String msg = "❤️ 用户 " + likerName + " 点赞了你在《" + movieTitle + "》下的评论";

                    // 5. 发送给评论作者 (review.getUserId())
                    // 这里的 WebSocketServer 需要引用你项目里的那个类
                    WebSocketServer.sendInfo(msg, review.getUserId().toString());

                } catch (Exception e) {
                    // 发送通知失败不应该影响点赞主流程，打印日志即可
                    e.printStackTrace();
                }
            }

            return newCount;
        }
    }
}
