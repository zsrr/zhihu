package com.stephen.zhihu.service;

import com.stephen.zhihu.dao.UserRepository;
import com.stephen.zhihu.domain_jpa.User;
import com.stephen.zhihu.dto.ThirdPartyInfo;
import com.stephen.zhihu.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class UserValidationServiceImpl implements UserValidationService {

    private UserRepository userDAO;
    private JedisPool jp;

    @Autowired
    public UserValidationServiceImpl(UserRepository userDAO, JedisPool jp) {
        this.userDAO = userDAO;
        this.jp = jp;
    }

    @Override
    public void registerValidation(User user, boolean includePassword) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        Set<ConstraintViolation<User>> usernameViolations = validator.validateProperty(user, "phone");
        Set<ConstraintViolation<User>> passwordViolations = validator.validateProperty(user, "password");

        if (!usernameViolations.isEmpty()) {
            throw new DataInvalidException();
        }

        if (includePassword && !passwordViolations.isEmpty()) {
            throw new DataInvalidException();
        }

        if (userDAO.hasUser(user.getPhone())) {
            throw new DuplicatedUserException();
        }
    }

    @Override
    public void isUserInVerifiedList(String phone) {
        try (Jedis jedis = jp.getResource()) {
            if (!jedis.sismember("zhihu-verified-phone-number", phone)) {
                throw new UserNotVerifiedException();
            }
        }
    }

    @Override
    public void loginValidation(String phone) {
        if (!userDAO.hasUser(phone)) {
            throw new NotFoundException();
        }
    }

    @Override
    public void loginValidation(Long id) {
        if (!userDAO.hasUser(id)) {
            throw new NotFoundException();
        }
    }

    @Override
    public void bindQQValidation(String qq) {
        if (userDAO.isQQBound(qq)) {
            throw new QQAccountAlreadyBoundException();
        }
    }

    @Override
    public void bindWechatValidation(String wechat) {
        if (userDAO.isWechatIdBound(wechat)) {
            throw new WechatAlreadyBoundException();
        }
    }

    @Override
    public void thirdPartyInfoValidation(ThirdPartyInfo info) {
        if (info == null || info.getOpenId() == null) {
            throw new DataInvalidException();
        }
    }

    @Override
    public void loginByQQValidation(String openId) {
        if (!userDAO.isQQBound(openId)) {
            throw new NotFoundException();
        }
    }

    @Override
    public void loginByWechatValidation(String openId) {
        if (!userDAO.isWechatIdBound(openId)) {
            throw new NotFoundException();
        }
    }
}
