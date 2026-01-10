package com.movie.repository;

import com.movie.entity.es.ActorDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ActorEsRepository extends ElasticsearchRepository<ActorDoc, Long> {
    List<ActorDoc> findByName(String name);
}
