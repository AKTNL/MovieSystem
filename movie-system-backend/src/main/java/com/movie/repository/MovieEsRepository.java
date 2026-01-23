package com.movie.repository;

import com.movie.entity.es.MovieDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface MovieEsRepository extends ElasticsearchRepository<MovieDoc, Long> {
    // 自动生成查询逻辑：找标题或简介包含关键词的
    List<MovieDoc> findByTitleOrSynopsis(String title, String synopsis);

    List<MovieDoc> findByTitleOrSynopsisOrGenre(String title, String synopsis, String genre);
}
