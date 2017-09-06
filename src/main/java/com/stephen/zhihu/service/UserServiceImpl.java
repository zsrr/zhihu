package com.stephen.zhihu.service;

import com.stephen.zhihu.dao.UserRepository;
import com.stephen.zhihu.domain.User;
import com.stephen.zhihu.dto.BaseResponse;
import com.stephen.zhihu.dto.RegisterResponse;
import com.stephen.zhihu.dto.ThirdPartyInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional(isolation = Isolation.READ_COMMITTED)
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

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public BaseResponse bindQQ(Long userId, String qq, ThirdPartyInfo thirdPartyInfo) {
        User user = userDAO.getUser(userId);
        user.setQq(qq);
        if (user.getAvatar() == null) {
            user.setAvatar(thirdPartyInfo.getAvatar());
        }

        if (user.getNickname().startsWith("User-")) {
            user.setNickname(thirdPartyInfo.getNickname());
        }

        if (user.getGender() == null) {
            user.setGender(thirdPartyInfo.getGender());
        }

        return new BaseResponse();
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public BaseResponse bindWechat(Long userId, String wechat, ThirdPartyInfo thirdPartyInfo) {
        User user = userDAO.getUser(userId);
        user.setWechat(wechat);
        if (user.getAvatar() == null) {
            user.setAvatar(thirdPartyInfo.getAvatar());
        }

        if (user.getNickname().startsWith("User-")) {
            user.setNickname(thirdPartyInfo.getNickname());
        }

        if (user.getGender() == null) {
            user.setGender(thirdPartyInfo.getGender());
        }

        return new BaseResponse();
    }
}
