package com.stephen.zhihu.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "USER_RELATIONSHIPS")
public class UserRelationship {

    public UserRelationship() {
    }

    @Embeddable
    public static class Id implements Serializable {
        private static final long serialVersionUID = -3943752032199967130L;

        @Column(name = "A_ID", nullable = false)
        Long sender;
        @Column(name = "B_ID", nullable = false)
        Long target;

        public Id(Long sender, Long target) {
            this.sender = sender;
            this.target = target;
        }

        public Long getSender() {
            return sender;
        }

        public void setSender(Long sender) {
            this.sender = sender;
        }

        public Long getTarget() {
            return target;
        }

        public void setTarget(Long target) {
            this.target = target;
        }

        public Id() {
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Id id = (Id) o;

            if (sender != null ? !sender.equals(id.sender) : id.sender != null) return false;
            return target != null ? target.equals(id.target) : id.target == null;
        }

        @Override
        public int hashCode() {
            int result = sender != null ? sender.hashCode() : 0;
            result = 31 * result + (target != null ? target.hashCode() : 0);
            return result;
        }
    }

    @EmbeddedId
    protected Id id;

    boolean mutual = false;

    public UserRelationship(Id id) {
        this.id = id;
    }

    public Id getId() {
        return id;
    }

    public boolean isMutual() {
        return mutual;
    }

    public void setMutual(boolean mutual) {
        this.mutual = mutual;
    }
}
