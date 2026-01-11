package com.movie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.movie.entity.Movie;

public interface MovieService extends IService<Movie> {
    //新增带有演职员的电影
    void saveMovie(Movie movie);
    //修改带有演职员的电影
    void updateMovie(Movie movie);
    //重新计算某部电影的评分
    void refreshRating(Long movieId);
}
