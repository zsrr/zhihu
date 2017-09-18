package com.stephen.zhihu.elasticsearch_repository;

import com.stephen.zhihu.domain_elasticsearch.TopicDoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TopicDocRepository extends ElasticsearchRepository<TopicDoc, String> {
    Page<TopicDoc> findByTopicName(String topicName, Pageable pageable);
}
