package com.movie.controller;

import com.movie.common.Auth;
import com.movie.common.Result;
import com.movie.entity.Actor;
import com.movie.entity.ActorVo;
import com.movie.entity.Movie;
import com.movie.mapper.ActorMapper;
import com.movie.mapper.DirectorMapper;
import com.movie.mapper.MovieMapper;
import com.movie.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/actors")
public class ActorController {
    @Autowired
    private ActorService actorService;

    @Autowired
    private ActorMapper actorMapper;

    @Autowired
    private MovieMapper movieMapper;

    @GetMapping("/list")
    public Result<List<Actor>> list(){
        List<Actor> list = actorService.list();
        return Result.success(list);
    }

    // 获取某电影的演员表
    @GetMapping("/{movieId}")
    public Result<List<ActorVo>> getActors(@PathVariable Long movieId) {
        return Result.success(actorMapper.selectActorsByMovieId(movieId));
    }

    //添加演员
    @Auth("admin")
    @PostMapping("/add")
    public Result<?> add(@RequestBody Actor actor){
        actorService.save(actor);
        return Result.success(null);
    }

    //修改演员
    @Auth("admin")
    @PutMapping("/update")
    public Result<?> update(@RequestBody Actor actor){
        actorService.updateById(actor);
        return Result.success(null);
    }

    //删除演员
    @Auth("admin")
    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id){
        actorService.removeById(id);
        return Result.success(null);
    }

    // 获取演员详情 + 参演电影
    @GetMapping("/detail/{id}")
    public Result<Map<String, Object>> getActorDetail(@PathVariable Long id) {
        // 1. 查演员基本信息
        Actor actor = actorService.getById(id);
        if (actor == null) {
            return Result.error("演员不存在");
        }

        // 2. 查参演电影
        List<Movie> movies = movieMapper.selectMoviesByActorId(id);

        // 3. 封装返回
        Map<String, Object> map = new HashMap<>();
        map.put("actor", actor);
        map.put("movies", movies);
        return Result.success(map);
    }
}
