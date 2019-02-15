package com.corkili.learningserver.common;

import com.corkili.learningserver.generate.protobuf.Response.BaseResponse;
import com.corkili.learningserver.generate.protobuf.Response.ResponseCode;
import com.corkili.learningserver.token.TokenManager;

public class ControllerUtils {

    public static BaseResponse generateSuccessBaseResponse(String token, String msg) {
        return BaseResponse.newBuilder()
                .setToken(token)
                .setResult(true)
                .setMsg(msg)
                .setResponseCode(ResponseCode.SUCCESS)
                .build();
    }

    public static BaseResponse generateErrorBaseResponse(String token, String msg) {
        return BaseResponse.newBuilder()
                .setToken(token)
                .setResult(false)
                .setMsg(msg)
                .setResponseCode(ResponseCode.GENERAL_ERROR)
                .build();
    }

    public static BaseResponse generateBaseResponseFrom(String token, ServiceResult serviceResult) {
        if (serviceResult == null) {
            return BaseResponse.newBuilder().build();
        }
        return BaseResponse.newBuilder()
                .setToken(token)
                .setResult(serviceResult.result())
                .setMsg(serviceResult.msg())
                .setResponseCode(serviceResult.isSuccess() ? ResponseCode.SUCCESS : ResponseCode.GENERAL_ERROR)
                .build();
    }

    public static BaseResponse validateTokenLogin(TokenManager tokenManager, String token) {
        if (!tokenManager.isLogin(token)) {
            return BaseResponse.newBuilder()
                    .setToken(token)
                    .setResult(false)
                    .setMsg("Don't login")
                    .setResponseCode(ResponseCode.UNAUTHORIZED)
                    .build();
        }
        return null;
    }

}
