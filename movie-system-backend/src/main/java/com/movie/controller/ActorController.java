package com.movie.controller;

import com.movie.common.Result;
import com.movie.entity.Actor;
import com.movie.entity.ActorVo;
import com.movie.mapper.ActorMapper;
import com.movie.mapper.DirectorMapper;
import com.movie.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/actors")
public class ActorController {
    @Autowired
    private ActorService actorService;

    @Autowired
    private ActorMapper actorMapper;

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
}
