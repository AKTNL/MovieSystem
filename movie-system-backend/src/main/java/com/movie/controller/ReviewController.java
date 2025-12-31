package com.movie.controller;

import com.movie.common.Result;
import com.movie.entity.Review;
import com.movie.entity.ReviewVo;
import com.movie.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    //提交评论
    @PostMapping("/add")
    public Result<?> add(@RequestBody Review review){
        try{
            // 前端传来的数据包含: userId, movieId, score, content
            reviewService.save(review);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error("提交失败" + e.getMessage());
        }
    }

    // 获取某电影的评论列表
    @GetMapping("list/{movieId}")
    public Result<List<ReviewVo>> list(@PathVariable Long movieId){
        List<ReviewVo> list = reviewService.getReviewsByMovieId(movieId);
        return Result.success(list);
    }
}
