package com.stephen.zhihu.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.stephen.zhihu.domain_jpa.User;
import com.stephen.zhihu.dto.*;

public interface UserService {
    VerificationSMSResponse registerSendSMSCode(String phone);
    BaseResponse registerVerifyCode(String phone, String msgId, String code);
    RegisterResponse initPassword(String phone, String password);

    LoginResponse loginByPassword(String phone, String password);
    LoginResponse loginByQQ(String openId);
    LoginResponse loginByWechat(String openId);

    BaseResponse bindQQ(Long userId, ThirdPartyInfo info);
    BaseResponse bindWechat(Long userId, ThirdPartyInfo info);

    User getUser(Long userId);
    BaseResponse update(Long userId, ObjectNode node) throws JsonProcessingException;

    BaseResponse addFollower(Long userId, Long targetUserId);
}
