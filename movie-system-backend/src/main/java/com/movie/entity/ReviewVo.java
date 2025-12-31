package com.movie.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewVo {
    private Long reviewId;
    private String username;
    private String avatar;
    private Integer score;
    private String content;
    private LocalDateTime createTime;
}
