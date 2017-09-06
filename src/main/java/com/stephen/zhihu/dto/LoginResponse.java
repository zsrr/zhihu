package com.stephen.zhihu.dto;

import com.stephen.zhihu.domain.User;

public class LoginResponse extends BaseResponse {
    User user;
    String token;

    public LoginResponse(User user, String token) {
        this.token = token;
    }

    public LoginResponse() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
