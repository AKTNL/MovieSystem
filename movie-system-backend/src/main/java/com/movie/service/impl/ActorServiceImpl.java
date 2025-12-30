package com.movie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.movie.entity.Actor;
import com.movie.mapper.ActorMapper;
import com.movie.service.ActorService;
import org.springframework.stereotype.Service;

@Service
public class ActorServiceImpl extends ServiceImpl<ActorMapper, Actor> implements ActorService {
}
