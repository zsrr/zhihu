package com.stephen.zhihu.dao;

import com.stephen.zhihu.domain.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

@Repository
public class UserRepositoryImpl extends BaseRepository implements UserRepository {

    @Autowired
    public UserRepositoryImpl(SessionFactory sf) {
        super(sf);
    }

    @Override
    public boolean hasUser(String phone) {
        Session session = getCurrentSession();
        TypedQuery<Long> userQuery = session.createQuery("select u.id from User u where u.phone = :phone").setParameter("phone", phone);
        try {
            Long id = userQuery.getSingleResult();
            return id != null;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    public boolean hasUser(Long id) {
        Session session = getCurrentSession();
        try {
            User user = session.getReference(User.class, id);
            return user != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isQQBound(String qq) {
        Session session = getCurrentSession();
        TypedQuery<Long> userQuery = session.createQuery("select u.id from User u where u.qqOpenId = :qq").setParameter("qq", qq);
        try {
            Long id = userQuery.getSingleResult();
            return id != null;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    public boolean isWechatIdBound(String wechat) {
        Session session = getCurrentSession();
        TypedQuery<Long> userQuery = session.createQuery("select u.id from User u where u.wechatOpenId = :wechat").setParameter("wechat", wechat);
        try {
            Long id = userQuery.getSingleResult();
            return id != null;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    public User register(String phone, String password, String nickname) {
        Session session = getCurrentSession();
        User user = new User(phone, password, nickname);
        session.persist(user);
        return user;
    }

    @Override
    public User getUser(Long id) {
        Session session = getCurrentSession();
        return session.get(User.class, id);
    }

    @Override
    public User getUser(String phone) {
        Session session = getCurrentSession();
        TypedQuery<User> query = session.createQuery("select u from User u where u.phone = :phone").setParameter("phone", phone);
        return query.getSingleResult();
    }

    @Override
    public User getUserByQQ(String openId) {
        Session session = getCurrentSession();
        TypedQuery<User> query = session.createQuery("select u from User u where u.qqOpenId = :openId").setParameter("openId", openId);
        return query.getSingleResult();
    }

    @Override
    public User getUserByWechat(String openId) {
        Session session = getCurrentSession();
        TypedQuery<User> query = session.createQuery("select u from User u where u.wechatOpenId = :openId").setParameter("openId", openId);
        return query.getSingleResult();
    }
}
