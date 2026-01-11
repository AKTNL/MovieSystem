package com.movie.controller;

import com.movie.common.Auth;
import com.movie.common.Result;
import com.movie.entity.AdminReviewVo;
import com.movie.entity.Review;
import com.movie.entity.ReviewVo;
import com.movie.mapper.ReviewMapper;
import com.movie.service.MovieService;
import com.movie.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ReviewMapper reviewMapper;
    @Autowired
    private MovieService movieService;

    //提交评论
    @PostMapping("/add")
    public Result<?> add(@RequestBody Review review){
        try{
            // 前端传来的数据包含: userId, movieId, score, content
            reviewService.save(review);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error("提交失败" + e.getMessage());
        }
    }

    // 获取某电影的评论列表
    @GetMapping("/list/{movieId}")
    public Result<List<ReviewVo>> list(@PathVariable Long movieId){
        List<ReviewVo> list = reviewService.getReviewsByMovieId(movieId);
        return Result.success(list);
    }

    //获取所有评论 (管理员用)
    @Auth("admin")
    @GetMapping("/listAll")
    public Result<List<AdminReviewVo>> listAll(){
        return Result.success(reviewMapper.selectAllReviews());
    }

    //删除评论
    @Auth("admin")
    @DeleteMapping("/admin/delete/{id}")
    public Result<?> delete(@PathVariable Long id){
        Review review = reviewService.getById(id);
        Long movieId = review.getMovieId();
        reviewService.removeById(id);
        movieService.refreshRating(movieId);
        return Result.success(null);
    }

    // 用户修改自己的评论
    @PostMapping("/update")
    public Result<?> update(@RequestBody Review review, @RequestHeader(value = "userId", required = false) String userIdStr) {
        // 1. 先查一下这个评论存在不存在
        Review oldReview = reviewService.getById(review.getReviewId());
        if (oldReview == null) {
            return Result.error("评论不存在");
        }

        // 从Header获取当前登录人ID，而不是信任前端传来的body
        // 如果Header里没传userId，说明可能没登录或Token有问题
        if (userIdStr == null) {
            return Result.error("未登录或身份异常");
        }

        // 比对：当前登录人 vs 评论作者
        if (!oldReview.getUserId().toString().equals(userIdStr)) {
            return Result.error("无权修改他人的评论");
        }

        // 确保 ID 和 UserId 不被篡改，只更新内容和分数
        review.setUserId(oldReview.getUserId());

        reviewService.updateById(review);
        return Result.success(null);
    }

    // 用户删除自己的评论
    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id, @RequestHeader(value = "userId", required = false) String userIdStr) {
        // 1. 查是否存在
        Review review = reviewService.getById(id);
        if (review == null) {
            return Result.success(null);
        }

        Long movieId = review.getMovieId();

        // 2. 校验身份
        if (userIdStr != null) {
            Long currentUserId = Long.parseLong(userIdStr);
            // 判断是不是作者
            if (!review.getUserId().equals(currentUserId)) {
                return Result.error("无权删除");
            }
        } else {
            return Result.error("未登录");
        }

        reviewService.removeById(id);
        movieService.refreshRating(movieId);
        return Result.success(null);
    }
}
