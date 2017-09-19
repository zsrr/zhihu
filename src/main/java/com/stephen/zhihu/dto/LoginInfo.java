package com.stephen.zhihu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginInfo {

    @JsonProperty("open_id")
    String openId;

    @JsonProperty("access_token")
    String accessToken;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
