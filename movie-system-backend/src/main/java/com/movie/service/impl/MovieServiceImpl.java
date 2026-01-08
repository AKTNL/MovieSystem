package com.movie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.movie.entity.Movie;
import com.movie.entity.MovieActor;
import com.movie.entity.MovieDirector;
import com.movie.mapper.MovieActorMapper;
import com.movie.mapper.MovieDirectorMapper;
import com.movie.mapper.MovieMapper;
import com.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovieServiceImpl extends ServiceImpl<MovieMapper, Movie> implements MovieService {
    @Autowired
    private MovieActorMapper movieActorMapper;
    @Autowired
    private MovieDirectorMapper movieDirectorMapper;

    @Override
    @Transactional // 开启事务，保证同时成功或同时失败
    public void saveMovie(Movie movie){
        // 1.保存电影信息
        save(movie);

        // 2.保存电影和演员的关联关系
        if(movie.getActorIds() != null){
            for(Long actorId : movie.getActorIds()){
                movieActorMapper.insert(new MovieActor(movie.getMovieId(), actorId));
            }
        }

        // 3.保存电影和导演的关联关系
        if(movie.getDirectorIds() != null){
            for(Long directorId : movie.getDirectorIds()){
                movieDirectorMapper.insert(new MovieDirector(movie.getMovieId(), directorId));
            }
        }
    }

    @Override
    @Transactional
    public void updateMovie(Movie movie){
        //1.更新电影信息
        updateById(movie);

        //2.删除电影和演员的关联关系
        QueryWrapper<MovieActor> actorWrapper = new QueryWrapper<>();
        actorWrapper.eq("movie_id", movie.getMovieId());
        movieActorMapper.delete(actorWrapper);

        // 插入新演员
        if(movie.getActorIds() != null){
            for(Long actorId : movie.getActorIds()){
                movieActorMapper.insert(new MovieActor(movie.getMovieId(), actorId));
            }
        }

        //3.删除电影和导演的关联关系
        QueryWrapper<MovieDirector> directorWrapper = new QueryWrapper<>();
        directorWrapper.eq("movie_id", movie.getMovieId());
        movieDirectorMapper.delete(directorWrapper);

        // 插入新导演
        if(movie.getDirectorIds() != null){
            for(Long directorId : movie.getDirectorIds()){
                movieDirectorMapper.insert(new MovieDirector(movie.getMovieId(), directorId));
            }
        }
    }
}
