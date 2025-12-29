package com.movie.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("movie")
public class Movie {
    @TableId(type = IdType.AUTO) // 设置主键自增
    private Long movieId;
    private String title;
    private Integer releaseYear;
    private Integer duration;
    private String genre;
    private String language;
    private String country;
    private String synopsis;
    private String coverUrl;
    private BigDecimal rating;
    private Integer ratingCount;
    private LocalDateTime createTime;
}
