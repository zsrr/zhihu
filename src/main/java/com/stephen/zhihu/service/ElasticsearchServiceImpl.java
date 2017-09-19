package com.stephen.zhihu.service;

import com.stephen.zhihu.Constants;
import com.stephen.zhihu.domain_elasticsearch.QuestionDoc;
import com.stephen.zhihu.domain_elasticsearch.TopicDoc;
import com.stephen.zhihu.domain_elasticsearch.UserDoc;
import com.stephen.zhihu.elasticsearch_repository.QuestionDocRepository;
import com.stephen.zhihu.elasticsearch_repository.TopicDocRepository;
import com.stephen.zhihu.elasticsearch_repository.UserDocRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {

    private QuestionDocRepository questionDocRepository;
    private UserDocRepository userDocRepository;
    private TopicDocRepository topicDocRepository;

    @Autowired
    public ElasticsearchServiceImpl(QuestionDocRepository questionDocRepository,
                                    UserDocRepository userDocRepository,
                                    TopicDocRepository topicDocRepository) {
        this.questionDocRepository = questionDocRepository;
        this.userDocRepository = userDocRepository;
        this.topicDocRepository = topicDocRepository;
    }

    @Override
    @Async(Constants.ASYNC_EXECUTOR)
    public void saveQuestionDoc(QuestionDoc doc) {
        questionDocRepository.save(doc);
    }

    @Override
    @Async(Constants.ASYNC_EXECUTOR)
    public void saveUserDoc(UserDoc doc) {
        userDocRepository.save(doc);
    }

    @Override
    @Async(Constants.ASYNC_EXECUTOR)
    public void saveTopicDoc(TopicDoc doc) {
        topicDocRepository.save(doc);
    }
}
