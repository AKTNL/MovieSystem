package com.movie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.movie.entity.MovieActor;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MovieActorMapper extends BaseMapper<MovieActor> {
}
