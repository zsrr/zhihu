package com.stephen.zhihu.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "USER", indexes = {@Index(name = "phone", columnList = "PHONE", unique = true),
        @Index(name = "qq", columnList = "QQ_ACCOUNT", unique = true),
        @Index(name = "wechat", columnList = "WECHAT_ID", unique = true)})
public class User {

    public enum Gender {
        MALE,
        FEMALE
    }

    @Id
    @GeneratedValue(generator = Constants.PERFECT_SEQUENCE)
    protected Long id;

    @Column(name = "PHONE", nullable = false, unique = true, length = 15)
    @Pattern(regexp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$")
    @NotNull
    protected String phone;

    @Column(name = "QQ_ACCOUNT", unique = true, length = 15)
    @Pattern(regexp = "[1-9][0-9]{4,14}")
    protected String qq;

    @Column(name = "WECHAT_ID", unique = true, length = 30)
    @Pattern(regexp = "^[a-z_\\d]+$")
    protected String wechat;

    /**
     * Md5加密字符串  32位
     */
    @Column(nullable = false, length = 35)
    @NotNull
    @Size(min = 32, max = 32)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    protected String password;

    @Column(length = 120)
    protected String avatar;

    @Column(length = 30, nullable = false)
    @NotNull
    protected String nickname;

    @Enumerated(EnumType.STRING)
    protected Gender gender;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public Long getId() {
        return id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public User() {
    }

    public User(String phone, String password, String nickname) {
        this.phone = phone;
        this.password = password;
        this.nickname = nickname;
    }
}
