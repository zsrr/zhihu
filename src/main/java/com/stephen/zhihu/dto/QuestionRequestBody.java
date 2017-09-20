package com.stephen.zhihu.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class QuestionRequestBody {

    @JsonProperty(required = true)
    String title;

    @JsonProperty(required = true)
    String description;

    List<Long> topics;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Long> getTopics() {
        return topics;
    }

    public void setTopics(List<Long> topics) {
        this.topics = topics;
    }
}
