package com.stephen.zhihu.service;

import com.stephen.zhihu.dao.UserRepository;
import com.stephen.zhihu.domain.User;
import com.stephen.zhihu.exception.DuplicatedUserException;
import com.stephen.zhihu.exception.QQAccountAlreadyBoundException;
import com.stephen.zhihu.exception.UserInfoInvalidException;
import com.stephen.zhihu.exception.WechatAlreadyBoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class UserValidationServiceImpl implements UserValidationService {

    private UserRepository userDAO;

    @Autowired
    public UserValidationServiceImpl(UserRepository userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public void registerValidation(User user) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        Set<ConstraintViolation<User>> usernameViolations = validator.validateProperty(user, "phone");
        Set<ConstraintViolation<User>> passwordViolations = validator.validateProperty(user, "password");

        if (!usernameViolations.isEmpty() ||
                !passwordViolations.isEmpty()) {
            throw new UserInfoInvalidException();
        }

        if (userDAO.hasUser(user.getPhone())) {
            throw new DuplicatedUserException();
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
}
