package com.movie.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    //用于接收前端传来的ID数组
    @TableField(exist = false) // 告诉MyBatis这是非数据库字段
    private List<MovieActorParam> actorList;
    @TableField(exist = false) // 告诉MyBatis这是非数据库字段
    private List<Long> directorIds;

    // 定义内部类用于接收参数
    @Data
    public static class MovieActorParam {
        private Long actorId;
        private String roleName;
    }
}
