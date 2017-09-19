package com.stephen.zhihu.domain_jpa;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

// 应该用权限将管理用户和普通用户区分开来，管理员用户可以增加话题
@Entity
@Table(name = "TOPICS", indexes = @Index(name = "title", columnList = "TITLE", unique = true))
public class Topic {

    @Id
    @GeneratedValue(generator = Constants.PERFECT_SEQUENCE)
    protected Long id;

    @Column(name = "TITLE", unique = true, nullable = false, length = 15)
    protected String title;

    @Column(length = 150)
    protected String intro;

    protected String icon;

    @ManyToOne(fetch = FetchType.LAZY)
    protected Topic upperTopic;

    @OneToMany
    @JoinTable(name = "TOPIC_HIERARCHY",
            joinColumns = @JoinColumn(name = "UPPER_TOPIC_ID"),
            inverseJoinColumns = @JoinColumn(name = "CHILD_TOPIC_ID"))
    protected Set<Topic> childTopics = new HashSet<>();

    @ManyToMany(mappedBy = "topics")
    protected Set<Question> questions = new HashSet<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Long getId() {
        return id;
    }

    public Topic getUpperTopic() {
        return upperTopic;
    }

    public void setUpperTopic(Topic upperTopic) {
        this.upperTopic = upperTopic;
    }

    public Set<Topic> getChildTopics() {
        return childTopics;
    }

    public Set<Question> getQuestions() {
        return questions;
    }
}
