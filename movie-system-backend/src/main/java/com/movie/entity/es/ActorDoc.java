package com.movie.entity.es;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "actor_index")
public class ActorDoc {
    @Id
    private Long id;

    @Field(type = FieldType.Text)//姓名支持搜索
    private String name;

    @Field(type = FieldType.Keyword)
    private String avatarUrl;
}
