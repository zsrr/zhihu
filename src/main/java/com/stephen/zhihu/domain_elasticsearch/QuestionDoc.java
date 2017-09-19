package com.stephen.zhihu.domain_elasticsearch;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "zhihu", type = "questions")
public class QuestionDoc {

    @Id
    @Field(type = FieldType.Long)
    Long id;

    @Field(type = FieldType.String, analyzer = "smartcn", searchAnalyzer = "smartcn")
    String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
