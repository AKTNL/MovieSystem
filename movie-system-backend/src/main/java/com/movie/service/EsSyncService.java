package com.movie.service;

import cn.hutool.core.bean.BeanUtil;
import com.movie.entity.Actor;
import com.movie.entity.Director;
import com.movie.entity.Movie;
import com.movie.entity.es.ActorDoc;
import com.movie.entity.es.DirectorDoc;
import com.movie.entity.es.MovieDoc;
import com.movie.repository.ActorEsRepository;
import com.movie.repository.DirectorEsRepository;
import com.movie.repository.MovieEsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EsSyncService {
    @Autowired
    private MovieService movieService;
    @Autowired
    private DirectorService directorService;
    @Autowired
    private ActorService actorService;
    @Autowired
    private MovieEsRepository movieEsRepo;
    @Autowired
    private DirectorEsRepository directorEsRepo;
    @Autowired
    private ActorEsRepository actorEsRepo;

    // 全量同步方法：把所有数据从数据库读出来，写入 ES
    public void syncAll() {
        // 1. 同步电影
        List<Movie> movies = movieService.list();
        List<MovieDoc> movieDocs = movies.stream().map(m -> {
            MovieDoc doc = new MovieDoc();
            // 属性拷贝: id, title, genre... 名字一样的会自动拷过去
            BeanUtil.copyProperties(m, doc);
            // 注意：Movie实体里叫 movieId，ES文档里叫 id，如果名字不一样需要手动设置
            doc.setId(m.getMovieId());
            return doc;
        }).collect(Collectors.toList());
        movieEsRepo.saveAll(movieDocs);

        // 2. 同步演员
        List<Actor> actors = actorService.list();
        List<ActorDoc> actorDocs = actors.stream().map(a -> {
            ActorDoc doc = new ActorDoc();
            BeanUtil.copyProperties(a, doc);
            doc.setId(a.getActorId());
            return doc;
        }).collect(Collectors.toList());
        actorEsRepo.saveAll(actorDocs);

        // 3. 同步导演
        List<Director> directors = directorService.list();
        List<DirectorDoc> directorDocs = directors.stream().map(d -> {
            DirectorDoc doc = new DirectorDoc();
            BeanUtil.copyProperties(d, doc);
            doc.setId(d.getDirectorId());
            return doc;
        }).collect(Collectors.toList());
        directorEsRepo.saveAll(directorDocs);
    }

}
