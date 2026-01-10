package com.movie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.movie.entity.Movie;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MovieMapper extends BaseMapper<Movie> {
    //根据演员ID查询电影列表
    @Select("select m.* from movie m " +
            "join movie_actor ma on m.movie_id = ma.movie_id " +
            "where ma.actor_id = #{actorId}")
    List<Movie> selectMoviesByActorId(Long actorId);

    //根据导演ID查询电影列表
    @Select("select m.* from movie m " +
            "join movie_director md on m.movie_id = md.movie_id " +
            "where md.director_id = #{directorId}")
    List<Movie> selectMoviesByDirectorId(Long directorId);
}
