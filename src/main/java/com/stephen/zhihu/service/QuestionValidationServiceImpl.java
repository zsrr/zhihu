package com.stephen.zhihu.service;

import com.stephen.zhihu.dao.QuestionRepository;
import com.stephen.zhihu.exception.NotFoundException;
import com.stephen.zhihu.exception.PermissionDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class QuestionValidationServiceImpl implements QuestionValidationService {

    private final QuestionRepository questionDAO;

    @Autowired
    public QuestionValidationServiceImpl(QuestionRepository questionDAO) {
        this.questionDAO = questionDAO;
    }

    @Override
    public void updateQuestionPermissionValidation(Long userId, Long questionId) {
        Long questionerId = questionDAO.getQuestionerId(questionId);
        if (!userId.equals(questionerId)) {
            throw new PermissionDeniedException();
        }
    }

    @Override
    public void answerQuestionPermissionValidation(Long userId, Long questionId) {
        Long questionerId = questionDAO.getQuestionerId(questionId);
        if (questionerId.equals(userId)) {
            throw new PermissionDeniedException();
        }
    }

    @Override
    public void questionValidation(Long questionId) {
        if (!questionDAO.hasQuestion(questionId)) {
            throw new NotFoundException();
        }
    }
}
