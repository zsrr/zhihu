package com.stephen.zhihu.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.stephen.zhihu.authorization.Authorization;
import com.stephen.zhihu.authorization.CurrentUserId;
import com.stephen.zhihu.domain.User;
import com.stephen.zhihu.dto.BaseResponse;
import com.stephen.zhihu.dto.FollowerResponse;
import com.stephen.zhihu.dto.ThirdPartyInfo;
import com.stephen.zhihu.service.UserService;
import com.stephen.zhihu.service.UserValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Authorization
public class UserController {

    private UserService userService;
    private UserValidationService validationService;

    @Autowired
    public UserController(UserService userService, UserValidationService validationService) {
        this.userService = userService;
        this.validationService = validationService;
    }

    @RequestMapping(value = "/qq", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> bindQQ(@CurrentUserId Long currentUserId,
                                               @RequestBody ThirdPartyInfo info) {
        validationService.thirdPartyInfoValidation(info);
        validationService.bindQQValidation(info.getOpenId());
        return new ResponseEntity<>(userService.bindQQ(currentUserId, info), HttpStatus.OK);
    }

    @RequestMapping(value = "/wechat", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> bindWechat(@CurrentUserId Long currentUserId,
                                                   @RequestBody ThirdPartyInfo info) {
        validationService.thirdPartyInfoValidation(info);
        validationService.bindWechatValidation(info.getOpenId());
        return new ResponseEntity<>(userService.bindWechat(currentUserId, info), HttpStatus.OK);
    }

    @RequestMapping(value = "/{userId}/info", method = RequestMethod.GET)
    public ResponseEntity<User> getInfo(@PathVariable("userId") Long userId) {
        return new ResponseEntity<>(userService.getUser(userId), HttpStatus.OK);
    }

    @RequestMapping(value = "/info", method = RequestMethod.PATCH)
    public ResponseEntity<BaseResponse> update(@CurrentUserId Long userId, @RequestBody ObjectNode node) throws JsonProcessingException {
        return new ResponseEntity<>(userService.update(userId, node), HttpStatus.OK);
    }

    @RequestMapping(value = "/followers", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> addFollowers(@CurrentUserId Long userId, @RequestParam("target") Long targetUser) {
        return new ResponseEntity<>(userService.addFollower(userId, targetUser), HttpStatus.CREATED);
    }
}
