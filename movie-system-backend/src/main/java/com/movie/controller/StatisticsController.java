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
        queryWrapper.select("genre");
        List<Movie> list = movieService.list(queryWrapper);

        //定义一个 Map 来计数，Key=类型名, Value=数量
        Map<String, Integer> countMap = new HashMap<>();

        for (Movie movie : list) {
            String genreStr = movie.getGenre();
            if (genreStr != null && !genreStr.isEmpty()) {
                // 【关键步骤】用 "/" 分割字符串
                // 这里的正则表达式 "[,/，]" 表示支持用 斜杠、中文逗号、英文逗号 分割，兼容性更好
                String[] genres = genreStr.split("[,/，]");

                for (String g : genres) {
                    String key = g.trim(); // 去掉前后空格
                    if (!key.isEmpty()) {
                        // 计数 +1
                        countMap.put(key, countMap.getOrDefault(key, 0) + 1);
                    }
                }
            }
        }

        //转换成 ECharts 需要的 List<Map> 格式
        // 将 Map 转为 List 进行排序
        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(countMap.entrySet());
        // 按值降序排序 (数量多的在前)
        sortedList.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        List<Map<String, Object>> pieData = new ArrayList<>();
        int limit = 8; // 只展示前8名
        int otherCount = 0; // 记录“其他”的数量

        for (int i = 0; i < sortedList.size(); i++) {
            if (i < limit) {
                // 前8名，正常添加
                Map<String, Object> item = new HashMap<>();
                item.put("name", sortedList.get(i).getKey());
                item.put("value", sortedList.get(i).getValue());
                pieData.add(item);
            } else {
                // 8名以后，累加到“其他”
                otherCount += sortedList.get(i).getValue();
            }
        }

        // 如果有“其他”，添加到列表最后
        if (otherCount > 0) {
            Map<String, Object> otherItem = new HashMap<>();
            otherItem.put("name", "其他");
            otherItem.put("value", otherCount);
            pieData.add(otherItem);
        }
        map.put("genreData", pieData);

        return Result.success(map);
    }
}
