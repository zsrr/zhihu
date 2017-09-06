package com.stephen.zhihu.service;

import com.stephen.zhihu.domain.User;

public interface UserValidationService {
    void registerValidation(User user);
    void bindQQValidation(String qq);
    void bindWechatValidation(String wechat);
}
