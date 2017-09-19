package com.stephen.zhihu.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.stephen.zhihu.authorization.Authorization;
import com.stephen.zhihu.authorization.CurrentUserId;
import com.stephen.zhihu.domain_jpa.User;
import com.stephen.zhihu.dto.*;
import com.stephen.zhihu.exception.ActionResolveException;
import com.stephen.zhihu.exception.ChannelInvalidException;
import com.stephen.zhihu.exception.DataInvalidException;
import com.stephen.zhihu.service.ThirdPartyService;
import com.stephen.zhihu.service.UserService;
import com.stephen.zhihu.service.UserValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private UserValidationService validationService;
    private ThirdPartyService thirdPartyService;

    @Autowired
    public UserController(UserService userService, UserValidationService validationService, ThirdPartyService thirdPartyService) {
        this.userService = userService;
        this.validationService = validationService;
        this.thirdPartyService = thirdPartyService;
    }

    @RequestMapping(value = "/{phoneNumber}", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> register(@PathVariable("phoneNumber") String phone,
                                                 @RequestParam("action") String action,
                                                 @RequestBody(required = false) RegisterInfo registerInfo) {
        checkRegisterInfoWithAction(action, registerInfo);

        if (action.equals("send")) {
            validationService.registerValidation(new User(phone, null), false);
            return new ResponseEntity<>(userService.registerSendSMSCode(phone), HttpStatus.OK);
        } else if (action.equals("verify")) {
            return new ResponseEntity<>(userService.registerVerifyCode(phone, registerInfo.getMsgId(),
                    registerInfo.getCode()), HttpStatus.OK);
        }

        User user = new User(phone, registerInfo.getPassword());
        validationService.isUserInVerifiedList(phone);
        validationService.registerValidation(user, true);
        return new ResponseEntity<>(userService.initPassword(phone, registerInfo.getPassword()), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{identifier}", method = RequestMethod.GET)
    public ResponseEntity<LoginResponse> login(@PathVariable("identifier") String identifier,
                                               @RequestParam("channel") String channel,
                                               @RequestBody LoginInfo info) throws IOException {
        checkLoginInfoWithChannel(channel, info);

        if (channel.equals("phone")) {
            validationService.loginValidation(identifier);
            return new ResponseEntity<>(userService.loginByPassword(identifier, info.getPassword()), HttpStatus.OK);
        } else if (channel.equals("qq")) {
            validationService.loginByQQValidation(info.getOpenId());
            thirdPartyService.checkQQAccessToken(info.getOpenId(), info.getAccessToken());
            return new ResponseEntity<>(userService.loginByQQ(info.getOpenId()), HttpStatus.OK);
        }

        validationService.loginByWechatValidation(info.getOpenId());
        thirdPartyService.checkWechatAccessToken(info.getOpenId(), info.getAccessToken());
        return new ResponseEntity<>(userService.loginByWechat(info.getOpenId()), HttpStatus.OK);
    }

    @RequestMapping(value = "/current/qq", method = RequestMethod.POST)
    @Authorization
    public ResponseEntity<BaseResponse> bindQQ(@CurrentUserId Long currentUserId,
                                               @RequestBody ThirdPartyInfo info) {
        validationService.thirdPartyInfoValidation(info);
        validationService.bindQQValidation(info.getOpenId());
        return new ResponseEntity<>(userService.bindQQ(currentUserId, info), HttpStatus.OK);
    }

    @RequestMapping(value = "/current/wechat", method = RequestMethod.POST)
    @Authorization
    public ResponseEntity<BaseResponse> bindWechat(@CurrentUserId Long currentUserId,
                                                   @RequestBody ThirdPartyInfo info) {
        validationService.thirdPartyInfoValidation(info);
        validationService.bindWechatValidation(info.getOpenId());
        return new ResponseEntity<>(userService.bindWechat(currentUserId, info), HttpStatus.OK);
    }

    @RequestMapping(value = "/{userId}/info", method = RequestMethod.GET)
    @Authorization
    public ResponseEntity<User> getInfo(@PathVariable("userId") Long userId) {
        return new ResponseEntity<>(userService.getUser(userId), HttpStatus.OK);
    }

    @RequestMapping(value = "/current/info", method = RequestMethod.PATCH)
    @Authorization
    public ResponseEntity<BaseResponse> update(@CurrentUserId Long userId, @RequestBody ObjectNode node) throws JsonProcessingException {
        return new ResponseEntity<>(userService.update(userId, node), HttpStatus.OK);
    }

    @RequestMapping(value = "/current/followers/{targetUserId}", method = RequestMethod.POST)
    @Authorization
    public ResponseEntity<BaseResponse> addFollowers(@CurrentUserId Long userId, @PathVariable("targetUserId") Long targetUser) {
        return new ResponseEntity<>(userService.addFollower(userId, targetUser), HttpStatus.CREATED);
    }

    private static void checkRegisterInfoWithAction(String action, RegisterInfo info) {
        if (action.equals("verify")) {
            if (info == null || info.getCode() == null || info.getMsgId() == null) {
                throw new DataInvalidException();
            }
        } else if (action.equals("init")) {
            if (info == null || info.getPassword() == null) {
                throw new DataInvalidException();
            }
        } else if (!action.equals("send")) {
            throw new ActionResolveException(action);
        }
    }

    private static void checkLoginInfoWithChannel(String channel, LoginInfo info) {
        switch (channel) {
            case "phone":
                if (info == null || info.getPassword() == null) {
                    throw new DataInvalidException();
                }
                break;
            case "qq":
            case "wechat":
                if (info == null || info.getOpenId() == null || info.getAccessToken() == null) {
                    throw new DataInvalidException();
                }
                break;
            default:
                throw new ChannelInvalidException(channel);
        }
    }
}
