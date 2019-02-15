package com.corkili.learningserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.corkili.learningserver.bo.User;
import com.corkili.learningserver.common.ControllerUtils;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.generate.protobuf.Info.UserInfo;
import com.corkili.learningserver.generate.protobuf.Info.UserType;
import com.corkili.learningserver.generate.protobuf.Request.UserLoginRequest;
import com.corkili.learningserver.generate.protobuf.Request.UserRegisterRequest;
import com.corkili.learningserver.generate.protobuf.Response.UserLoginResponse;
import com.corkili.learningserver.generate.protobuf.Response.UserRegisterResponse;
import com.corkili.learningserver.service.UserService;
import com.corkili.learningserver.token.TokenManager;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/register", produces = "application/x-protobuf", method = RequestMethod.POST)
    public UserRegisterResponse register(@RequestBody UserRegisterRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        ServiceResult registerResult = userService.register(request.getUserInfo().getPhone(),
                request.getPassword(), request.getUserInfo().getUsername(),
                User.Type.valueOf(request.getUserInfo().getUserType().name()));
        User user = (User) registerResult.extra(User.class);
        UserInfo userInfo;
        if (registerResult.isSuccess() && user != null) {
            userInfo = UserInfo.newBuilder()
                    .setPhone(user.getPhone())
                    .setUsername(user.getUsername())
                    .setUserType(UserType.valueOf(user.getUserType().name()))
                    .build();
        } else {
            userInfo = request.getUserInfo();
        }
        return UserRegisterResponse.newBuilder()
                .setResponse(ControllerUtils.generateBaseResponseFrom(token, registerResult))
                .setUserInfo(userInfo)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/login", produces = "application/x-protobuf", method = RequestMethod.POST)
    public UserLoginResponse login(@RequestBody UserLoginRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        ServiceResult loginResult = userService.login(request.getPhone(), request.getPassword(),
                User.Type.valueOf(request.getUserType().name()));
        User user = (User) loginResult.extra(User.class);
        UserInfo userInfo;
        if (loginResult.isSuccess() && user != null) {
            userInfo = UserInfo.newBuilder()
                    .setPhone(user.getPhone())
                    .setUsername(user.getUsername())
                    .setUserType(UserType.valueOf(user.getUserType().name()))
                    .build();
            tokenManager.setTokenAssociatedUserAndLogin(token, user.getId());
        } else {
            userInfo = UserInfo.newBuilder()
                    .setPhone(request.getPhone())
                    .setUserType(request.getUserType())
                    .build();
        }
        return UserLoginResponse.newBuilder()
                .setResponse(ControllerUtils.generateBaseResponseFrom(token, loginResult))
                .setUserInfo(userInfo)
                .build();
    }


}
