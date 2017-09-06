package com.stephen.zhihu.service;

public interface JSMSService {
    String sendValidCode(String phone);
    boolean isCodeValid(String msgId, String code);
}
