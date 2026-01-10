package com.movie.controller;

import com.movie.common.Auth;
import com.movie.common.Result;
import com.movie.entity.Director;
import com.movie.entity.Movie;
import com.movie.mapper.DirectorMapper;
import com.movie.mapper.MovieMapper;
import com.movie.service.DirectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/directors")
public class DirectorController {
    @Autowired
    private DirectorService directorService;

    @Autowired
    private DirectorMapper directorMapper;

    @Autowired
    private MovieMapper movieMapper;

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

    //新增导演
    @Auth("admin")
    @PostMapping("/add")
    public Result<?> add(@RequestBody Director director){
        directorService.save(director);
        return Result.success(null);
    }

    //修改导演
    @Auth("admin")
    @PutMapping("/update")
    public Result<?> update(@RequestBody Director director){
        directorService.updateById(director);
        return Result.success(null);
    }

    //删除导演
    @Auth("admin")
    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id){
        directorService.removeById(id);
        return Result.success(null);
    }

    // 获取导演详情 + 执导电影
    @GetMapping("/detail/{id}")
    public Result<Map<String, Object>> getDirectorDetail(@PathVariable Long id) {
        Director director = directorService.getById(id);
        if (director == null) return Result.error("导演不存在");

        List<Movie> movies = movieMapper.selectMoviesByDirectorId(id);

        Map<String, Object> map = new HashMap<>();
        map.put("director", director);
        map.put("movies", movies);
        return Result.success(map);
    }
}
