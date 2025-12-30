package com.movie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.movie.entity.Director;
import com.movie.mapper.DirectorMapper;
import com.movie.service.DirectorService;
import org.springframework.stereotype.Service;

@Service
public class DirectorServiceImpl extends ServiceImpl<DirectorMapper, Director> implements DirectorService {
}
