package com.stephen.zhihu.dao;

import com.stephen.zhihu.domain_jpa.Question;
import com.stephen.zhihu.dto.QuestionRequestBody;

public interface QuestionRepository {
    Question getQuestion(Long id);

    Long addQuestion(Long userId, QuestionRequestBody body);
}
