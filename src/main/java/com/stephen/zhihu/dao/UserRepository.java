package com.stephen.zhihu.dao;

import com.stephen.zhihu.domain.User;

public interface UserRepository {
    boolean hasUser(String phone);
    boolean isQQBound(String qq);
    boolean isWechatIdBound(String wechat);
    User register(String phone, String password, String nickname);
    User getUser(Long id);
}
