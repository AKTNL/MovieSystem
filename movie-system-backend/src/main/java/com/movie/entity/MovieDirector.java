package com.movie.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("movie_director")
public class MovieDirector {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long movieId;
    private Long directorId;

    public MovieDirector(Long movieId, Long directorId) {
        this.movieId = movieId;
        this.directorId = directorId;
    }
}
