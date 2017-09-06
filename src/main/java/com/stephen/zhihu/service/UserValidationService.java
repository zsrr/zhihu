package com.stephen.zhihu.service;

import com.stephen.zhihu.domain.User;

public interface UserValidationService {
    void registerValidation(User user, boolean includePassword);
    void isUserInVerifiedList(String phone);
    void bindQQValidation(String qq);
    void bindWechatValidation(String wechat);
}
