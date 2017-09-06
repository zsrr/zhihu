package com.stephen.zhihu.dto;

public class VerificationSMSResponse extends BaseResponse {
    String msgId;

    public VerificationSMSResponse(String msgId) {
        this.msgId = msgId;
    }

    public VerificationSMSResponse() {
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}
