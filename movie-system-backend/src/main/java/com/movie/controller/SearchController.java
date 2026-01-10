package com.movie.controller;

import com.movie.common.Result;
import com.movie.entity.es.ActorDoc;
import com.movie.entity.es.DirectorDoc;
import com.movie.entity.es.MovieDoc;
import com.movie.repository.ActorEsRepository;
import com.movie.repository.DirectorEsRepository;
import com.movie.repository.MovieEsRepository;
import com.movie.service.EsSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private EsSyncService esSyncService;
    @Autowired
    private MovieEsRepository movieEsRepo;
    @Autowired
    private DirectorEsRepository directorEsRepo;
    @Autowired
    private ActorEsRepository actorEsRepo;

    // 管理员手动触发全量同步
    @PostMapping("/sync")
    public Result<?> sync() {
        esSyncService.syncAll();
        return Result.success("同步完成");
    }

    // 全局搜索接口
    @GetMapping("/all")
    public Result<Map<String, Object>> searchAll(@RequestParam String keyword) {
        Map<String, Object> result = new HashMap<>();

        // 1. 搜电影 (标题 或 简介)
        List<MovieDoc> movies = movieEsRepo.findByTitleOrSynopsis(keyword, keyword);
        result.put("movies", movies);

        // 2. 搜演员 (姓名)
        List<ActorDoc> actors = actorEsRepo.findByName(keyword);
        result.put("actors", actors);

        // 3. 搜导演 (姓名)
        List<DirectorDoc> directors = directorEsRepo.findByName(keyword);
        result.put("directors", directors);

        return Result.success(result);
    }
}
