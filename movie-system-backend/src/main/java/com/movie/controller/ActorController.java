package com.movie.controller;

import com.movie.common.Result;
import com.movie.entity.Actor;
import com.movie.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/actor")
public class ActorController {
    @Autowired
    private ActorService actorService;

    @GetMapping("/list")
    public Result<List<Actor>> list(){
        List<Actor> list = actorService.list();
        return Result.success(list);
    }
}
