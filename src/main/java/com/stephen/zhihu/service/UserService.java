package com.stephen.zhihu.service;

import com.stephen.zhihu.dto.*;

public interface UserService {
    VerificationSMSResponse registerSendSMSCode(String phone);
    BaseResponse registerVerifyCode(String phone, String msgId, String code);
    RegisterResponse initPassword(String phone, String password);

    LoginResponse loginByPassword(String phone, String password);
    LoginResponse loginByPassword(Long userId, String password);
    LoginResponse loginByQQ(String openId);
    LoginResponse loginByWechat(String openId);

    BaseResponse bindQQ(Long userId, ThirdPartyInfo info);
    BaseResponse bindWechat(Long userId, ThirdPartyInfo info);
}
