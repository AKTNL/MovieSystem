package com.movie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.movie.entity.Movie;
import com.movie.entity.Review;
import com.movie.entity.ReviewVo;
import com.movie.mapper.MovieMapper;
import com.movie.mapper.ReviewMapper;
import com.movie.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements ReviewService {
    @Autowired
    private MovieMapper movieMapper;

    @Override
    public List<ReviewVo> getReviewsByMovieId(Long movieId){
        //1.先查电影信息拿到标题
        Movie movie = movieMapper.selectById(movieId);
        if(movie == null){
            throw new RuntimeException("电影不存在");
        }
        //2.调用Mapper执行存储过程
        return baseMapper.selectReviewsByMovieTitle(movie.getTitle());
    }
}
