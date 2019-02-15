package com.corkili.learningserver.common;

import com.corkili.learningserver.generate.protobuf.Response.BaseResponse;
import com.corkili.learningserver.generate.protobuf.Response.ResponseCode;

public class ControllerUtils {

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

}
