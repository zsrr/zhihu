package com.stephen.zhihu.web;

import com.stephen.zhihu.authorization.Authorization;
import com.stephen.zhihu.authorization.CurrentUserId;
import com.stephen.zhihu.dto.BaseResponse;
import com.stephen.zhihu.dto.QuestionRequestBody;
import com.stephen.zhihu.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/questions")
@Authorization
public class QuestionController {

    QuestionService service;

    @Autowired
    public QuestionController(QuestionService service) {
        this.service = service;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> askQuestion(@CurrentUserId Long userId, @RequestBody QuestionRequestBody body) {
        return new ResponseEntity<>(service.askQuestion(userId, body), HttpStatus.CREATED);
    }

}
