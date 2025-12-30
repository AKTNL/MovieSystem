package com.movie.controller;

import com.movie.common.Result;
import com.movie.entity.Director;
import com.movie.service.DirectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/director")
public class DirectorController {
    @Autowired
    private DirectorService directorService;

    @GetMapping("/list")
    public Result<List<Director>> list(){
        List<Director> list = directorService.list();
        return Result.success(list);
    }
}
