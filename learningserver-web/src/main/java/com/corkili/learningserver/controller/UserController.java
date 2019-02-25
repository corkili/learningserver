package com.corkili.learningserver.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.corkili.learningserver.bo.User;
import com.corkili.learningserver.common.ControllerUtils;
import com.corkili.learningserver.common.ProtoUtils;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.common.ServiceUtils;
import com.corkili.learningserver.generate.protobuf.Info.UserInfo;
import com.corkili.learningserver.generate.protobuf.Request.UserLoginRequest;
import com.corkili.learningserver.generate.protobuf.Request.UserLogoutRequest;
import com.corkili.learningserver.generate.protobuf.Request.UserRegisterRequest;
import com.corkili.learningserver.generate.protobuf.Request.UserUpdateInfoRequest;
import com.corkili.learningserver.generate.protobuf.Response.BaseResponse;
import com.corkili.learningserver.generate.protobuf.Response.UserLoginResponse;
import com.corkili.learningserver.generate.protobuf.Response.UserLogoutResponse;
import com.corkili.learningserver.generate.protobuf.Response.UserRegisterResponse;
import com.corkili.learningserver.generate.protobuf.Response.UserUpdateInfoResponse;
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
        User user = registerResult.extra(User.class);
        UserInfo userInfo;
        if (registerResult.isSuccess() && user != null) {
            userInfo = ProtoUtils.generateUserInfo(user);
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
        User user = loginResult.extra(User.class);
        UserInfo userInfo;
        if (loginResult.isSuccess() && user != null) {
            userInfo = ProtoUtils.generateUserInfo(user);
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

    @ResponseBody
    @RequestMapping(value = "/logout", produces = "application/x-protobuf", method = RequestMethod.POST)
    public UserLogoutResponse logout(@RequestBody UserLogoutRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse;
        if (tokenManager.isLogin(token)) {
            baseResponse = ControllerUtils.generateSuccessBaseResponse("", "logout success");
        } else {
            baseResponse = ControllerUtils.generateErrorBaseResponse("", "already logout");
        }
        tokenManager.removeToken(token);
        return UserLogoutResponse.newBuilder()
                .setResponse(baseResponse)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/updateInfo", produces = "application/x-protobuf", method = RequestMethod.POST)
    public UserUpdateInfoResponse updateUserInfo(@RequestBody UserUpdateInfoRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        Optional<User> userOptional;
        if (tokenManager.isLogin(token)) {
            userOptional = userService.retrieve(tokenManager.getUserIdAssociatedWithToken(token));
        } else {
            userOptional = userService.retrieveUserByPhoneAndUserType(request.getPhone(),
                    User.Type.valueOf(request.getUserType().name()));
        }
        BaseResponse baseResponse;
        UserInfo userInfo;
        if (!userOptional.isPresent()) {
            baseResponse = ControllerUtils.generateErrorBaseResponse(token,
                    ServiceUtils.format("user [{}@{}] not exists", request.getPhone(), request.getUserType()));
            userInfo = UserInfo.newBuilder().build();
        } else {
            User user = userOptional.get();
            User copyUser = User.copyFrom(user);
            if (request.getUpdatePhone()) {
                copyUser.setPhone(request.getNewPhone());
            }
            if (request.getUpdateUsername()) {
                copyUser.setUsername(request.getNewUsername());
            }
            if (request.getUpdatePassword()) {
                copyUser.setPassword(request.getNewPassword());
                copyUser.setRawPassword(true);
            }
            ServiceResult serviceResult = userService.modifyUserInfo(copyUser);
            baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
            userInfo = ProtoUtils.generateUserInfo(serviceResult.extra(User.class));
        }
        return UserUpdateInfoResponse.newBuilder()
                .setResponse(baseResponse)
                .setUserInfo(userInfo)
                .build();
    }

}
