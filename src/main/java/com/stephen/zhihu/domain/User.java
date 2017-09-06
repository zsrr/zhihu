package com.stephen.zhihu.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "USER")
public class User {

    public enum Gender {
        MALE,
        FEMALE
    }

    @Id
    @GeneratedValue(generator = Constants.PERFECT_SEQUENCE)
    protected Long id;

    @Column(nullable = false, unique = true, length = 15)
    @Pattern(regexp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$")
    @NotNull
    protected String phone;

    @Column(unique = true, length = 15)
    @Pattern(regexp = "[1-9][0-9]{4,14}")
    protected String qq;

    @Column(unique = true, length = 30)
    @Pattern(regexp = "^[a-z_\\d]+$")
    protected String wechat;

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
}
