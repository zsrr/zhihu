package com.stephen.zhihu.elasticsearch_repository;

import com.stephen.zhihu.domain_elasticsearch.QuestionDoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface QuestionDocRepository extends ElasticsearchRepository<QuestionDoc, String> {
    Page<QuestionDoc> findByTitle(String title, Pageable pageable);
}
