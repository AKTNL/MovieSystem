package com.movie.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminReviewVo {
    private Long reviewId;
    private String movieTitle;
    private String username;
    private Integer score;
    private String content;
    private LocalDateTime createTime;
}
