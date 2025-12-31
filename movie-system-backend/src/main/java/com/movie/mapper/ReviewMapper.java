package com.movie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
}
