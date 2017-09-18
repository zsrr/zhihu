package com.stephen.zhihu.domain_jpa;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Education {
    @Column(length = 20, nullable = false)
    protected String university;
    @Column(length = 10, nullable = false)
    protected String major;

    public Education() {
    }

    public Education(String university, String majorl) {
        this.university = university;
        this.major = majorl;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String majorl) {
        this.major = majorl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Education education = (Education) o;

        if (university != null ? !university.equals(education.university) : education.university != null) return false;
        return major != null ? major.equals(education.major) : education.major == null;
    }

    @Override
    public int hashCode() {
        int result = university != null ? university.hashCode() : 0;
        result = 31 * result + (major != null ? major.hashCode() : 0);
        return result;
    }
}
