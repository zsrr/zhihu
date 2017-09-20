package com.stephen.zhihu.service;

import com.stephen.zhihu.dao.QuestionRepository;
import com.stephen.zhihu.domain_elasticsearch.QuestionDoc;
import com.stephen.zhihu.dto.BaseResponse;
import com.stephen.zhihu.dto.QuestionRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuestionServiceImpl implements QuestionService {

    QuestionRepository repository;
    ElasticsearchService elasticsearchService;

    @Autowired
    public QuestionServiceImpl(QuestionRepository repository, ElasticsearchService elasticsearchService) {
        this.repository = repository;
        this.elasticsearchService = elasticsearchService;
    }

    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public BaseResponse askQuestion(Long userId, QuestionRequestBody body) {
        Long id = repository.addQuestion(userId, body);

        QuestionDoc doc = new QuestionDoc();
        doc.setId(id);
        doc.setTitle(body.getTitle());

        elasticsearchService.saveQuestionDoc(doc);
        return new BaseResponse(HttpStatus.CREATED, null);
    }
}
