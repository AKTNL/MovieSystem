package com.movie.repository;

import com.movie.entity.es.DirectorDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface DirectorEsRepository extends ElasticsearchRepository<DirectorDoc, Long> {
    List<DirectorDoc> findByName(String name);
}
