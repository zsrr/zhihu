package com.stephen.zhihu.domain_jpa;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "QUESTIONS", indexes = @Index(name = "title_idx", columnList = "TITLE"))
public class Question {

    @Id
    @GeneratedValue(generator = Constants.PERFECT_SEQUENCE)
    protected Long id;

    @Column(nullable = false, length = 50)
    @Size(min = 10, max = 50)
    @NotNull
    protected String title;

    @Lob
    protected String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, name = "USER_ID")
    protected User user;

    @ManyToMany(mappedBy = "questionsCaredAbout")
    protected Set<User> usersConcerned = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "TOPIC_TO_QUESTIONS",
            joinColumns = @JoinColumn(name = "COMMENT_ID"),
            inverseJoinColumns = @JoinColumn(name = "TOPIC_ID"))
    protected Set<Topic> topics = new HashSet<>();

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<User> getUsersConcerned() {
        return usersConcerned;
    }
}
