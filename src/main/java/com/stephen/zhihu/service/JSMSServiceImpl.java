package com.stephen.zhihu.service;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jsms.api.JSMSClient;
import cn.jsms.api.SendSMSResult;
import cn.jsms.api.ValidSMSResult;
import cn.jsms.api.common.model.SMSPayload;
import com.stephen.zhihu.exception.JGException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JSMSServiceImpl implements JSMSService {

    private JSMSClient client;

    @Autowired
    public JSMSServiceImpl(JSMSClient client) {
        this.client = client;
    }

    @Override
    public String sendValidCode(String phone) {
        SMSPayload payload = SMSPayload.newBuilder().
                setMobileNumber(phone).
                setTempId(1).
                setTTL(60).build();
        try {
            SendSMSResult result = client.sendSMSCode(payload);
            return result.getMessageId();
        } catch (APIConnectionException | APIRequestException e) {
            throw new JGException(e);
        }
    }

    @Override
    public boolean isCodeValid(String msgId, String code) {
        try {
            ValidSMSResult result = client.sendValidSMSCode(msgId, code);
            return result.getIsValid();
        } catch (APIConnectionException | APIRequestException e) {
            throw new JGException(e);
        }
    }
}
