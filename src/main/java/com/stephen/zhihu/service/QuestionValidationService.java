package com.stephen.zhihu.service;

public interface QuestionValidationService {
    void isQuestionMadeByUser(Long userId, Long questionId);
}
