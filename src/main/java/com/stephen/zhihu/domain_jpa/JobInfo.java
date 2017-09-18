package com.stephen.zhihu.domain_jpa;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class JobInfo {
    @Column(length = 20)
    protected String company;
    @Column(length = 10)
    protected String position;

    public JobInfo() {
    }

    public JobInfo(String company, String position) {
        this.company = company;
        this.position = position;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
