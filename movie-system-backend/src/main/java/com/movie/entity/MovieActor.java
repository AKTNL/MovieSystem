package com.movie.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("movie_actor")
public class MovieActor {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long movieId;
    private Long actorId;
    private String roleName;

    public MovieActor(Long movieId, Long actorId) {
        this.movieId = movieId;
        this.actorId = actorId;
    }
}
