package com.stephen.zhihu.service;

import com.stephen.zhihu.domain.User;
import com.stephen.zhihu.dto.ThirdPartyInfo;

public interface UserValidationService {
    void registerValidation(User user, boolean includePassword);
    void isUserInVerifiedList(String phone);
    void loginValidation(String phone);
    void loginValidation(Long id);
    void currentUserValidation(Long userId, Long currentUserId);
    void bindQQValidation(String qqOpenId);
    void bindWechatValidation(String wechatOpenId);
    void thirdPartyInfoValidation(ThirdPartyInfo info);
    void loginByQQValidation(String openId);
    void loginByWechatValidation(String openId);
}
