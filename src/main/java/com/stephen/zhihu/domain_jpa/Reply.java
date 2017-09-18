package com.stephen.zhihu.domain_jpa;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Reply {

    @Id
    @GeneratedValue(generator = Constants.PERFECT_SEQUENCE)
    protected Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    protected User user;

    public Long getId() {
        return id;
    }

    @Lob
    @NotNull
    protected String content;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    protected Date lastModified = new Date();

    @ManyToMany(mappedBy = "agreedReplies")
    protected Set<User> agreedUsers = new HashSet<>();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Set<User> getAgreedUsers() {
        return agreedUsers;
    }
}
