package com.stephen.zhihu.service;

import com.stephen.zhihu.dto.BaseResponse;
import com.stephen.zhihu.dto.QuestionRequestBody;

public interface QuestionService {
    BaseResponse askQuestion(Long userId, QuestionRequestBody body);
}
