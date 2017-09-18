package com.stephen.zhihu.domain_jpa;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

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

    /**
     * Md5加密字符串  32位
     */
    @Column(nullable = false, length = 35)
    @NotNull
    @Size(min = 32, max = 32)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    protected String password;

    protected String avatar;

    @Column(length = 15, nullable = false)
    @NotNull
    protected String nickname;

    @Column(name = "QQ_OPEN_ID")
    protected String qqOpenId;

    @Column(name = "WECHAT_OPEN_ID")
    protected String wechatOpenId;

    @Enumerated(EnumType.STRING)
    protected Gender gender;

    @Column(length = 50)
    protected String tags;

    @Column(length = 150)
    protected String profile;

    @Column(length = 20)
    protected String industry;

    protected Address location;

    protected JobInfo jobInfo;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "EDUCATION_INFO")
    protected Set<Education> educations = new HashSet<>();

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getQqOpenId() {
        return qqOpenId;
    }

    public void setQqOpenId(String qqOpenId) {
        this.qqOpenId = qqOpenId;
    }

    public String getWechatOpenId() {
        return wechatOpenId;
    }

    public void setWechatOpenId(String wechatOpenId) {
        this.wechatOpenId = wechatOpenId;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public User() {
    }

    public User(String phone, String password, String nickname) {
        this.phone = phone;
        this.password = password;
        this.nickname = nickname;
    }

    public User(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }

    public Set<Education> getEducations() {
        return educations;
    }

    public Address getLocation() {
        return location;
    }

    public void setLocation(Address location) {
        this.location = location;
    }

    public JobInfo getJobInfo() {
        return jobInfo;
    }

    public void setJobInfo(JobInfo jobInfo) {
        this.jobInfo = jobInfo;
    }
}
