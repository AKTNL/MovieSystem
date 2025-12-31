package com.movie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.movie.entity.Review;
import com.movie.entity.ReviewVo;

import java.util.List;

public interface ReviewService extends IService<Review> {
    List<ReviewVo> getReviewsByMovieId(Long movieId);
}
