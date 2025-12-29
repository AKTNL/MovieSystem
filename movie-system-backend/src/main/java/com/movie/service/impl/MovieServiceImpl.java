package com.movie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.movie.entity.Movie;
import com.movie.mapper.MovieMapper;
import com.movie.service.MovieService;
import org.springframework.stereotype.Service;

@Service
public class MovieServiceImpl extends ServiceImpl<MovieMapper, Movie> implements MovieService {

}
