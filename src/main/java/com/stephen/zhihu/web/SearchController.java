package com.stephen.zhihu.web;

import com.stephen.zhihu.service.ElasticsearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final ElasticsearchService searchService;

    @Autowired
    public SearchController(ElasticsearchService searchService) {
        this.searchService = searchService;
    }

    public ResponseEntity question() {
        return null;
    }

    public ResponseEntity user() {
        return null;
    }

    public ResponseEntity topic() {
        return null;
    }
}
