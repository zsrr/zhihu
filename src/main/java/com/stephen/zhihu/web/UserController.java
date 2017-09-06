package com.stephen.zhihu.web;

import com.stephen.zhihu.authorization.Authorization;
import com.stephen.zhihu.authorization.CurrentUserId;
import com.stephen.zhihu.dto.BaseResponse;
import com.stephen.zhihu.dto.ThirdPartyInfo;
import com.stephen.zhihu.service.UserService;
import com.stephen.zhihu.service.UserValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Authorization
public class UserController {

    private UserService userService;
    private UserValidationService validationService;

    @Autowired
    public UserController(UserService userService, UserValidationService validationService) {
        this.userService = userService;
        this.validationService = validationService;
    }

    @RequestMapping(value = "/{userId}/qq", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> bindQQ(@PathVariable("userId") Long userId,
                                               @CurrentUserId Long currentUserId,
                                               @RequestBody ThirdPartyInfo info) {
        validationService.currentUserValidation(userId, currentUserId);
        validationService.thirdPartyInfoValidation(info);
        validationService.bindQQValidation(info.getOpenId());
        return new ResponseEntity<>(userService.bindQQ(userId, info), HttpStatus.OK);
    }

    @RequestMapping(value = "/{userId}/wechat", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> bindWechat(@PathVariable("userId") Long userId,
                                               @CurrentUserId Long currentUserId,
                                               @RequestBody ThirdPartyInfo info) {
        validationService.currentUserValidation(userId, currentUserId);
        validationService.thirdPartyInfoValidation(info);
        validationService.bindWechatValidation(info.getOpenId());
        return new ResponseEntity<>(userService.bindWechat(userId, info), HttpStatus.OK);
    }
}
