package com.stephen.zhihu.web;

import com.stephen.zhihu.dto.LoginResponse;
import com.stephen.zhihu.service.ThirdPartyService;
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

import java.io.IOException;

@RestController
@RequestMapping("/login")
public class LoginController {
    private UserService userService;
    private UserValidationService validationService;
    private ThirdPartyService thirdPartyService;

    @Autowired
    public LoginController(UserService userService, UserValidationService validationService, ThirdPartyService thirdPartyService) {
        this.userService = userService;
        this.validationService = validationService;
        this.thirdPartyService = thirdPartyService;
    }

    @RequestMapping(value = "/byPassword", method = RequestMethod.GET, consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<LoginResponse> loginByPassword(@RequestParam(value = "phone", required = false) String phone,
                                                         @RequestParam(value = "id", required = false) Long id,
                                                         @RequestParam("password") String password) throws MissingServletRequestParameterException {
        if (phone == null && id == null) {
            throw new MissingServletRequestParameterException("phone or id", "String or Long");
        }

        if (phone != null) {
            validationService.loginValidation(phone);
            return new ResponseEntity<>(userService.loginByPassword(phone, password), HttpStatus.OK);
        }

        validationService.loginValidation(id);
        return new ResponseEntity<>(userService.loginByPassword(id, password), HttpStatus.OK);
    }

    @RequestMapping(value = "/byQQ", method = RequestMethod.GET, consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<LoginResponse> loginByQQ(@RequestParam("openId") String openId, @RequestParam("access_token") String authorizationToken) throws IOException {
        validationService.loginByQQValidation(openId);
        thirdPartyService.checkQQAccessToken(openId, authorizationToken);
        return new ResponseEntity<>(userService.loginByQQ(openId), HttpStatus.OK);
    }

    @RequestMapping(value = "/byWechat", method = RequestMethod.GET, consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<LoginResponse> loginByWechat(@RequestParam("openId") String openId, @RequestParam("access_token") String authorizationToken) throws IOException {
        validationService.loginByWechatValidation(openId);
        thirdPartyService.checkWechatAccessToken(openId, authorizationToken);
        return new ResponseEntity<>(userService.loginByWechat(openId), HttpStatus.OK);
    }
}
