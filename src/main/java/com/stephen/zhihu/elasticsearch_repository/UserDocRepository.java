package com.stephen.zhihu.elasticsearch_repository;

import com.stephen.zhihu.domain_elasticsearch.UserDoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface UserDocRepository extends ElasticsearchRepository<UserDoc, String> {
    Page<UserDoc> findByUserName(String name, Pageable pageable);
}
