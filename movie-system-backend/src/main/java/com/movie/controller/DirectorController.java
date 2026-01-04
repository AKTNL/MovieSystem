package com.movie.controller;

import com.movie.common.Result;
import com.movie.entity.Director;
import com.movie.mapper.DirectorMapper;
import com.movie.service.DirectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/directors")
public class DirectorController {
    @Autowired
    private DirectorService directorService;

    @Autowired
    private DirectorMapper directorMapper;

    @GetMapping("/list")
    public Result<List<Director>> list(){
        List<Director> list = directorService.list();
        return Result.success(list);
    }

    // 获取某电影的导演
    @GetMapping("/{movieId}")
    public Result<List<Director>> getDirectors(@PathVariable Long movieId) {
        return Result.success(directorMapper.selectDirectorsByMovieId(movieId));
    }
}
