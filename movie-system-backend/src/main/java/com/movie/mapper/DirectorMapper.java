package com.movie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.movie.entity.Director;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DirectorMapper extends BaseMapper<Director> {
    // 根据电影ID查询导演
    @Select("SELECT d.* FROM director d " +
            "JOIN movie_director md ON d.director_id = md.director_id " +
            "WHERE md.movie_id = #{movieId}")
    List<Director> selectDirectorsByMovieId(Long movieId);
}
