package com.movie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.movie.common.Auth;
import com.movie.common.Result;
import com.movie.entity.Movie;
import com.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    //查询所有电影接口
    @GetMapping("/list")
    public Result<List<Movie>> list(
            @RequestParam(required = false) String title, // 电影名(模糊)
            @RequestParam(required = false) String genre // 类型(精确)
    ){
        QueryWrapper<Movie> queryWrapper = new QueryWrapper<>();

        //动态拼接SQL
        if (title != null && !title.isEmpty()){
            queryWrapper.like("title", title);
        }
        if (genre != null && !genre.isEmpty()){
            queryWrapper.eq("genre", genre);
        }

        // 默认按评分倒序（评分高的在前面）
        queryWrapper.orderByDesc("create_time");

        List<Movie> list = movieService.list(queryWrapper);
        return Result.success(list);
    }

    //根据ID查询电影详情
    @GetMapping("/{id}")
    public Result<Movie> getById(@PathVariable Long id){
        Movie movie = movieService.getById(id);
        return Result.success(movie);
    }

    //新增电影
    @Auth("admin")
    @PostMapping("/add")
    public Result<?> add(@RequestBody Movie movie){
        movieService.saveMovie(movie);
        return Result.success(null);
    }

    //修改电影
    @Auth("admin")
    @PutMapping("/update")
    public Result<?> update(@RequestBody Movie movie){
        movieService.updateMovie(movie);
        return Result.success(null);
    }

    //删除电影
    @Auth("admin")
    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id){
        movieService.removeById(id);
        return Result.success(null);
    }

    // 获取热门电影（按评分倒序取前10名）
    @GetMapping("/hot")
    public Result<List<Movie>> hot(){
        QueryWrapper<Movie> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("rating");
        queryWrapper.last("LIMIT 10");
        List<Movie> list = movieService.list(queryWrapper);
        return Result.success(list);
    }
}
