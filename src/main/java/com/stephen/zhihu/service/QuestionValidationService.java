package com.stephen.zhihu.service;

public interface QuestionValidationService {
    void updateQuestionPermissionValidation(Long userId, Long questionId);
    void answerQuestionPermissionValidation(Long userId, Long questionId);
    void questionValidation(Long questionId);
}
