package com.stephen.zhihu.dto;

public class RegisterResponse extends BaseResponse {
    Long id;

    public RegisterResponse() {
    }

    public RegisterResponse(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
