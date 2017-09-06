package com.stephen.zhihu.service;

import java.io.IOException;

public interface ThirdPartyService {
    void checkQQAccessToken(String openId, String accessToken) throws IOException;
    void checkWechatAccessToken(String openId, String accessToken) throws IOException;
}
