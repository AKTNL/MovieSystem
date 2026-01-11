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

    private Integer likeCount; // 总点赞数
    private Boolean isLiked;   // 当前登录用户是否已点赞
}
