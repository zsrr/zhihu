package com.stephen.zhihu.web;

import com.stephen.zhihu.domain.User;
import com.stephen.zhihu.exception.ActionResolveException;
import com.stephen.zhihu.service.UserService;
import com.stephen.zhihu.service.UserValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegisterController {
    private UserService userService;
    private UserValidationService validationService;

    @Autowired
    public RegisterController(UserService userService, UserValidationService validationService) {
        this.userService = userService;
        this.validationService = validationService;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded")
    public ResponseEntity register(@RequestParam("action") String action,
                                   @RequestParam("phone") String phone,
                                   @RequestParam(value = "password", required = false) String password,
                                   @RequestParam(value = "msgId", required = false) String msgId,
                                   @RequestParam(value = "code", required = false) String smsCode) throws MissingServletRequestParameterException {
        User user = new User(phone, password);
        if (action.equals("send")) {
            validationService.registerValidation(user, false);
            return new ResponseEntity(userService.registerSendSMSCode(phone), HttpStatus.OK);
        } else if (action.equals("verify")) {
            if (msgId == null) {
                throw new MissingServletRequestParameterException("msgId", "String");
            }

            if (smsCode == null) {
                throw new MissingServletRequestParameterException("code", "String");
            }
            validationService.registerValidation(user, false);
            return new ResponseEntity(userService.registerVerifyCode(phone, msgId, smsCode), HttpStatus.OK);
        } else if (action.equals("register")) {
            if (password == null) {
                throw new MissingServletRequestParameterException("password", "String");
            }

            validationService.registerValidation(user, true);
            validationService.isUserInVerifiedList(phone);
            return new ResponseEntity(userService.initPassword(phone, password), HttpStatus.CREATED);
        } else {
            throw new ActionResolveException("名为 " + action + " 的action无法解析");
        }
    }
}
