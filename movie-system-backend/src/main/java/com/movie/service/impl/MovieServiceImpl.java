package com.movie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.movie.entity.Movie;
import com.movie.entity.MovieActor;
import com.movie.entity.MovieDirector;
import com.movie.entity.Review;
import com.movie.mapper.MovieActorMapper;
import com.movie.mapper.MovieDirectorMapper;
import com.movie.mapper.MovieMapper;
import com.movie.mapper.ReviewMapper;
import com.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MovieServiceImpl extends ServiceImpl<MovieMapper, Movie> implements MovieService {
    @Autowired
    private MovieActorMapper movieActorMapper;
    @Autowired
    private MovieDirectorMapper movieDirectorMapper;
    @Autowired
    private ReviewMapper reviewMapper;

    @Override
    @Transactional // 开启事务，保证同时成功或同时失败
    public void saveMovie(Movie movie){
        // 1.保存电影信息
        save(movie);

        // 2.保存电影和演员的关联关系
        if(movie.getActorList() != null){
            for(Movie.MovieActorParam param : movie.getActorList()){
                MovieActor ma = new MovieActor(movie.getMovieId(), param.getActorId());
                ma.setRoleName(param.getRoleName()); // 设置角色名
                movieActorMapper.insert(ma);
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
        if(movie.getActorList() != null){
            for(Movie.MovieActorParam param : movie.getActorList()){
                MovieActor ma = new MovieActor(movie.getMovieId(), param.getActorId());
                ma.setRoleName(param.getRoleName());
                movieActorMapper.insert(ma);
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
    @Override
    public void refreshRating(Long movieId) {
        // 1. 计算总人数
        long count = reviewMapper.selectCount(new QueryWrapper<Review>().eq("movie_id", movieId));

        // 2. 计算平均分
        List<Review> reviews = reviewMapper.selectList(new QueryWrapper<Review>().eq("movie_id", movieId));
        double avg = 0.0;
        if (!reviews.isEmpty()) {
            double sum = reviews.stream().mapToInt(Review::getScore).sum();
            avg = Math.round((sum / count) * 10.0) / 10.0; // 保留1位小数
        }

        // 3. 更新电影表
        Movie movie = new Movie();
        movie.setMovieId(movieId);
        movie.setRatingCount((int) count);
        movie.setRating(BigDecimal.valueOf(avg));

        updateById(movie);
    }
}
