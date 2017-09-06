package com.stephen.zhihu.dto;

import com.stephen.zhihu.domain.User;


public class ThirdPartyInfo {
    String openId;
    String avatar;
    String nickname;
    User.Gender gender;

    public ThirdPartyInfo() {
    }

    public ThirdPartyInfo(String openId, String avatar, String nickname, User.Gender gender) {
        this.openId = openId;
        this.avatar = avatar;
        this.nickname = nickname;
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public User.Gender getGender() {
        return gender;
    }

    public void setGender(User.Gender gender) {
        this.gender = gender;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
