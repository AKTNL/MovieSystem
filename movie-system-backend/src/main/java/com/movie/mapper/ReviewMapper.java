package com.movie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.movie.entity.AdminReviewVo;
import com.movie.entity.Review;
import com.movie.entity.ReviewVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ReviewMapper extends BaseMapper<Review> {
    //调用存储过程GetMovieReviews
    @Select("call GetMovieReviews(#{title})")
    List<ReviewVo> selectReviewsByMovieTitle(@Param("title") String title);

    @Select("select r.review_id, m.title as movie_title, u.username, r.score, r.content, r.create_time " +
            "from review r " +
            "left join movie m on r.movie_id = m.movie_id " +
            "left join sys_user u on r.user_id = u.user_id " +
            "order by r.create_time desc ")
    List<AdminReviewVo> selectAllReviews();
}
