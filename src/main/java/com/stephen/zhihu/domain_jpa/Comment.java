package com.stephen.zhihu.domain_jpa;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Comment {
    @Id
    @GeneratedValue(generator = Constants.PERFECT_SEQUENCE)
    protected Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    protected User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    protected Reply reply;

    @Lob
    @NotNull
    protected String content;

    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdTime = new Date();

    @ManyToOne
    protected Comment replyTo;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "COMMENT_REPLIES",
            joinColumns = @JoinColumn(name = "COMMENT_ID"),
            inverseJoinColumns = @JoinColumn(name = "REPLY_COMMENT_ID"))
    protected Set<Comment> replies = new HashSet<>();

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Reply getReply() {
        return reply;
    }

    public void setReply(Reply reply) {
        this.reply = reply;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Comment getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(Comment replyTo) {
        this.replyTo = replyTo;
    }

    public Set<Comment> getReplies() {
        return replies;
    }
}
