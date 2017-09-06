package com.stephen.zhihu.service;

import com.stephen.zhihu.dto.BaseResponse;
import com.stephen.zhihu.dto.RegisterResponse;
import com.stephen.zhihu.dto.ThirdPartyInfo;

public interface UserService {
    RegisterResponse register(String phone, String password);
    BaseResponse bindQQ(Long userId, String qq, ThirdPartyInfo thirdPartyInfo);
    BaseResponse bindWechat(Long userId, String wechat, ThirdPartyInfo thirdPartyInfo);
}
