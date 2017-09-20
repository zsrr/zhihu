package com.stephen.zhihu.web;

import com.stephen.zhihu.authorization.Authorization;
import com.stephen.zhihu.authorization.CurrentUserId;
import com.stephen.zhihu.domain_jpa.Reply;
import com.stephen.zhihu.dto.BaseResponse;
import com.stephen.zhihu.dto.QuestionRequestBody;
import com.stephen.zhihu.service.QuestionService;
import com.stephen.zhihu.service.QuestionValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/questions")
@Authorization
public class QuestionController {

    QuestionService service;
    QuestionValidationService validationService;

    @Autowired
    public QuestionController(QuestionService service, QuestionValidationService validationService) {
        this.service = service;
        this.validationService = validationService;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> askQuestion(@CurrentUserId Long userId, @RequestBody QuestionRequestBody body) {
        return new ResponseEntity<>(service.askQuestion(userId, body), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{questionId}/replies", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> reply(@PathVariable("questionId") Long questionId, @RequestBody Reply reply, @CurrentUserId Long userId) {
        validationService.questionValidation(questionId);
        validationService.answerQuestionPermissionValidation(userId, questionId);
        return null;
    }

}
