package com.stephen.zhihu.domain_jpa;

import javax.persistence.*;

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
}
