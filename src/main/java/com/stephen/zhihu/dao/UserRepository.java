package com.stephen.zhihu.dao;

import com.stephen.zhihu.domain_jpa.User;

public interface UserRepository {
    boolean hasUser(String phone);
    boolean hasUser(Long id);
    boolean isQQBound(String qq);
    boolean isWechatIdBound(String wechat);
    User register(String phone, String password, String nickname);
    User getUser(Long id);
    User getUser(String phone);
    User getUserByQQ(String openId);
    User getUserByWechat(String openId);
    void update(User user);
    void addFollower(Long userId, Long targetUserId);
}
