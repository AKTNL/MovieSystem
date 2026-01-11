package com.movie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.movie.common.Auth;
import com.movie.common.Result;
import com.movie.entity.Movie;
import com.movie.service.MovieService;
import com.movie.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    //查询所有电影接口
    @GetMapping("/list")
    public Result<List<Movie>> list(
            @RequestParam(required = false) String title, // 电影名(模糊)
            @RequestParam(required = false) String genre, // 类型(精确)
            @RequestParam(required = false, defaultValue = "newest") String sort
    ){
        QueryWrapper<Movie> queryWrapper = new QueryWrapper<>();

        //动态拼接SQL
        if (title != null && !title.isEmpty()){
            queryWrapper.like("title", title);
        }
        if (genre != null && !genre.isEmpty()){
            queryWrapper.like("genre", genre);
        }

        // 处理排序
        if ("rating".equals(sort)) {
            queryWrapper.orderByDesc("rating"); // 按评分降序
        } else {
            queryWrapper.orderByDesc("release_year"); // 按年份降序 (或者 create_time)
        }

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
        String msg = "🎬 重磅新片上线：《" + movie.getTitle() + "》！快来抢先观看！";
        WebSocketServer.sendToAll(msg);
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

    // 获取所有电影类型（去重、拆分后）
    @GetMapping("/genres")
    public Result<List<String>> getGenres() {
        // 1. 只查询 genre 字段，减少数据库压力
        QueryWrapper<Movie> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("genre");
        // 过滤掉空的
        queryWrapper.isNotNull("genre").ne("genre", "");
        List<Movie> list = movieService.list(queryWrapper);

        // 2. 使用 Set 去重
        Set<String> genreSet = new HashSet<>();

        for (Movie movie : list) {
            String genreStr = movie.getGenre();
            if (genreStr != null) {
                // 按斜杠、逗号、空格拆分
                String[] split = genreStr.split("[,/，\\s]+");
                for (String s : split) {
                    if (!s.trim().isEmpty()) {
                        genreSet.add(s.trim());
                    }
                }
            }
        }

        // 3. 转为 List 并排序（可选，按拼音或首字母排会让前端更好看）
        List<String> result = new ArrayList<>(genreSet);
        Collections.sort(result);

        return Result.success(result);
    }
}
