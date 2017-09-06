package com.stephen.zhihu.dto;

import com.stephen.zhihu.domain.User;

import java.io.Serializable;

public class ThirdPartyMessage implements Serializable {

    private static final long serialVersionUID = 14L;

    String avatar;
    String nickname;
    User.Gender gender;

    public ThirdPartyMessage() {
    }

    public ThirdPartyMessage(String avatar, String nickname, User.Gender gender) {
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
}
