package com.stephen.zhihu.service;

import com.stephen.zhihu.domain_elasticsearch.QuestionDoc;
import com.stephen.zhihu.domain_elasticsearch.TopicDoc;
import com.stephen.zhihu.domain_elasticsearch.UserDoc;

public interface ElasticsearchService {
    void saveQuestionDoc(QuestionDoc doc);
    void saveUserDoc(UserDoc doc);
    void saveTopicDoc(TopicDoc doc);
}
