package com.stephen.zhihu.domain_elasticsearch;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "zhihu", type = "users")
public class UserDoc {
    @Field(type = FieldType.Long)
    Long id;

    @Field(type = FieldType.String, analyzer = "smartcn", searchAnalyzer = "smartcn")
    String userName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
