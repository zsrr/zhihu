package com.stephen.zhihu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterInfo {

    @JsonProperty("msg_id")
    String msgId;

    String code;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
