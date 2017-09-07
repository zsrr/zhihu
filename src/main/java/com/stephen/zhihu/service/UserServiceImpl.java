package com.stephen.zhihu.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.stephen.zhihu.authorization.TokenManager;
import com.stephen.zhihu.authorization.TokenModel;
import com.stephen.zhihu.dao.UserRepository;
import com.stephen.zhihu.domain.User;
import com.stephen.zhihu.dto.*;
import com.stephen.zhihu.exception.PasswordIncorrectException;
import com.stephen.zhihu.exception.SMSCodeNotCorrectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.util.Iterator;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userDAO;
    private JedisPool jp;
    private JSMSService jsmsService;
    private TokenManager tokenManager;

    @Autowired
    public UserServiceImpl(UserRepository userDAO, JedisPool jp, JSMSService jsmsService, TokenManager tokenManager) {
        this.userDAO = userDAO;
        this.jp = jp;
        this.jsmsService = jsmsService;
        this.tokenManager = tokenManager;
    }

    @Override
    public VerificationSMSResponse registerSendSMSCode(String phone) {
        String msgId = jsmsService.sendValidCode(phone);
        return new VerificationSMSResponse(msgId);
    }

    @Override
    public BaseResponse registerVerifyCode(String phone, String msgId, String code) {
        try (Jedis jedis = jp.getResource()) {
            if (jsmsService.isCodeValid(msgId, code)) {
                jedis.sadd("zhihu-verified-phone-number", phone);
                return new BaseResponse();
            } else {
                throw new SMSCodeNotCorrectException();
            }
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public RegisterResponse initPassword(String phone, String password) {
        try (Jedis jedis = jp.getResource()) {
            Transaction tx = jedis.multi();
            tx.incrBy("zhihu-user-count", 1);
            tx.get("zhihu-user-count");
            Long count = (Long) tx.exec().get(0);
            User user = userDAO.register(phone, password, "User-" + count);
            jedis.srem("zhihu-verified-phone-number", phone);
            return new RegisterResponse(user.getId());
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public LoginResponse loginByPassword(String phone, String password) {
        User user = userDAO.getUser(phone);
        if (!user.getPassword().equals(password)) {
            throw new PasswordIncorrectException();
        }
        TokenModel tm = tokenManager.createToken(user.getId());
        return new LoginResponse(user, tm.getToken());
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public LoginResponse loginByPassword(Long userId, String password) {
        User user = userDAO.getUser(userId);
        if (!user.getPassword().equals(password)) {
            throw new PasswordIncorrectException();
        }
        TokenModel tm = tokenManager.createToken(user.getId());
        return new LoginResponse(user, tm.getToken());
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public LoginResponse loginByQQ(String openId) {
        User user = userDAO.getUserByQQ(openId);
        TokenModel tm = tokenManager.createToken(user.getId());
        return new LoginResponse(user, tm.getToken());
    }

    @Override
    public LoginResponse loginByWechat(String openId) {
        User user = userDAO.getUserByWechat(openId);
        TokenModel tm = tokenManager.createToken(user.getId());
        return new LoginResponse(user, tm.getToken());
    }

    private void updateUserByThirdPartyInfo(User user, ThirdPartyInfo info) {
        if (user.getAvatar() == null && info.getAvatar() != null) {
            user.setAvatar(info.getAvatar());
        }
        if (user.getNickname().startsWith("User-") && info.getNickname() != null) {
            user.setNickname(info.getNickname());
        }
        if (user.getGender() == null && info.getGender() != null) {
            user.setGender(info.getGender());
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public BaseResponse bindQQ(Long userId, ThirdPartyInfo info) {
        User user = userDAO.getUser(userId);
        user.setQqOpenId(info.getOpenId());
        updateUserByThirdPartyInfo(user, info);
        return new BaseResponse();
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public BaseResponse bindWechat(Long userId, ThirdPartyInfo info) {
        User user = userDAO.getUser(userId);
        user.setWechatOpenId(info.getOpenId());
        updateUserByThirdPartyInfo(user, info);
        return new BaseResponse();
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public User getUser(Long userId) {
        return userDAO.getUser(userId);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public BaseResponse update(Long userId, ObjectNode node) throws JsonProcessingException {
        User user = userDAO.getUser(userId);
        user = merge(user, node);
        userDAO.update(user);
        // 密码更新操作
        if (node.has("password")) {

        }
        return new BaseResponse();
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public BaseResponse addFollower(Long userId, Long targetUserId) {
        userDAO.addFollower(userId, targetUserId);
        BaseResponse br = new BaseResponse();
        br.setStatus(HttpStatus.CREATED.value());
        return br;
    }

    private User merge(User targetUser, ObjectNode node) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode targetNode = objectMapper.convertValue(targetUser, ObjectNode.class);
        targetNode.put("password", targetUser.getPassword());

        Iterator<String> fields = node.fieldNames();
        while (fields.hasNext()) {
            String field = fields.next();
            JsonNode updateValue = node.get(field);
            targetNode.replace(field, updateValue);
        }

        return objectMapper.treeToValue(targetNode, User.class);
    }
}
