package com.movie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.movie.entity.Actor;
import com.movie.entity.ActorVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ActorMapper extends BaseMapper<Actor> {
    // 根据电影ID查询演员列表
    @Select("SELECT a.actor_id, a.name, a.avatar_url, ma.role_name " +
            "FROM actor a " +
            "JOIN movie_actor ma ON a.actor_id = ma.actor_id " +
            "WHERE ma.movie_id = #{movieId}")
    List<ActorVo> selectActorsByMovieId(Long movieId);
}
