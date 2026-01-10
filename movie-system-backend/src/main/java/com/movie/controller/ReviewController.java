package com.movie.controller;

import com.movie.common.Auth;
import com.movie.common.Result;
import com.movie.entity.AdminReviewVo;
import com.movie.entity.Review;
import com.movie.entity.ReviewVo;
import com.movie.mapper.ReviewMapper;
import com.movie.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ReviewMapper reviewMapper;

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

    //获取所有评论 (管理员用)
    @Auth("admin")
    @GetMapping("/listAll")
    public Result<List<AdminReviewVo>> listAll(){
        return Result.success(reviewMapper.selectAllReviews());
    }

    //删除评论
    @Auth("admin")
    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id){
        reviewService.removeById(id);
        return Result.success(null);
    }
}
