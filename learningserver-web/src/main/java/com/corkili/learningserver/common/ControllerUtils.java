package com.corkili.learningserver.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.corkili.learningserver.generate.protobuf.Info.Image;
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
            return BaseResponse.getDefaultInstance();
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

    public static Map<String, byte[]> generateImageMap(String entityName, Long userId, List<Image> imageList) {
        Map<String, byte[]> images = new HashMap<>();
        for (Image image : imageList) {
            if (image.getHasData()) {
                images.put(ImageUtils.getImagePath(entityName, image.getPath(), userId), image.getImage().toByteArray());
            }
        }
        return images;
    }

}
