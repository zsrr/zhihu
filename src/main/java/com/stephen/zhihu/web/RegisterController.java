package com.stephen.zhihu.web;

import com.stephen.zhihu.domain_jpa.User;
import com.stephen.zhihu.dto.BaseResponse;
import com.stephen.zhihu.dto.RegisterResponse;
import com.stephen.zhihu.dto.VerificationSMSResponse;
import com.stephen.zhihu.service.UserService;
import com.stephen.zhihu.service.UserValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<RegisterResponse> register(@RequestParam("phone") String phone, @RequestParam("password") String password) {
        User user = new User(phone, password);
        validationService.isUserInVerifiedList(phone);
        validationService.registerValidation(user, true);
        return new ResponseEntity<>(userService.initPassword(phone, password), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public ResponseEntity<VerificationSMSResponse> send(@RequestParam("phone") String phone) {
        validationService.registerValidation(new User(phone, null), false);
        return new ResponseEntity<>(userService.registerSendSMSCode(phone), HttpStatus.OK);
    }

    @RequestMapping(value = "/verify", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<BaseResponse> verify(@RequestParam("phone") String phone,
                                               @RequestParam("msgId") String msgId,
                                               @RequestParam("code") String smsCode) {
        return new ResponseEntity<>(userService.registerVerifyCode(phone, msgId, smsCode), HttpStatus.OK);
    }
}
