package com.stephen.zhihu.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class BaseRepository {
    protected SessionFactory sf;

    public BaseRepository(SessionFactory sf) {
        this.sf = sf;
    }

    protected Session getCurrentSession() {
        return sf.getCurrentSession();
    }
}
