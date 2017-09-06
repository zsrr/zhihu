package com.stephen.zhihu.service;

import com.stephen.zhihu.dao.UserRepository;
import com.stephen.zhihu.domain.User;
import com.stephen.zhihu.dto.RegisterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userDAO;
    private JedisPool jp;

    @Autowired
    public UserServiceImpl(UserRepository userDAO, JedisPool jp) {
        this.userDAO = userDAO;
        this.jp = jp;
    }

    @Override
    public RegisterResponse register(String phone, String password) {
        try (Jedis jedis = jp.getResource()) {
            Transaction tx = jedis.multi();
            tx.incrBy("zhihu-user-count", 1);
            tx.get("zhihu-user-count");
            Long count = (Long) tx.exec().get(0);
            User user = userDAO.register(phone, password, "User-" + count);
            return new RegisterResponse(user.getId());
        }
    }
}
