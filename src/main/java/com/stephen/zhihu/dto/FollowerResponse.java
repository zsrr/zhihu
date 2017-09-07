package com.stephen.zhihu.dto;

public class FollowerResponse extends BaseResponse {
    String avatar;
    String nickname;
    String profile;

    public FollowerResponse(String avatar, String nickname, String profile) {
        this.avatar = avatar;
        this.nickname = nickname;
        this.profile = profile;
    }

    public FollowerResponse() {
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

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
