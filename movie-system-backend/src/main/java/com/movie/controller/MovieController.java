package com.movie.controller;

import com.movie.common.Result;
import com.movie.entity.Movie;
import com.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    //查询所有电影接口
    @GetMapping("/list")
    public Result<List<Movie>> list(){
        List<Movie> list = movieService.list();
        return Result.success(list);
    }
}
