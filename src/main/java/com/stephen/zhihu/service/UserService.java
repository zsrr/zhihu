package com.stephen.zhihu.service;

import com.stephen.zhihu.dto.RegisterResponse;

public interface UserService {
    RegisterResponse register(String phone, String password);

}
