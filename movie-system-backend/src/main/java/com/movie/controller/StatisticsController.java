package com.movie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.movie.common.Result;
import com.movie.entity.Movie;
import com.movie.service.MovieService;
import com.movie.service.ReviewService;
import com.movie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stats")
public class StatisticsController {
    @Autowired
    private MovieService movieService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/summary")
    public Result<Map<String, Object>> getSummary(){
        Map<String, Object> map = new HashMap<>();
        //1.基础总数统计
        map.put("movieCount", movieService.count());
        map.put("userCount", userService.count());
        map.put("reviewCount", reviewService.count());

        //2.电影类型分布统计
        QueryWrapper<Movie> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("genre", "count(*) as count");
        queryWrapper.groupBy("genre");

        List<Map<String, Object>> genreList = movieService.listMaps(queryWrapper);
        List<Map<String, Object>> pieData = new ArrayList<>();
        for(Map<String, Object> item : genreList){
            Map<String, Object> dataItem = new HashMap<>();
            dataItem.put("name", item.get("genre"));
            dataItem.put("value", item.get("count"));
            pieData.add(dataItem);
        }
        map.put("genreData", pieData);

        return Result.success(map);
    }
}
