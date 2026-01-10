package com.movie.entity.es;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "movie_index")// 指定索引名
public class MovieDoc {
    @Id
    private Long id;

    @Field(type = FieldType.Text) //开启分词搜索
    private String title;

    @Field(type = FieldType.Keyword) // 不分词搜索,精确匹配
    private String genre;

    @Field(type = FieldType.Double)
    private Double rating;

    @Field(type = FieldType.Text)
    private String synopsis;

    @Field(type = FieldType.Keyword)
    private String coverUrl;

    @Field(type = FieldType.Integer)
    private Integer releaseYear;
}
