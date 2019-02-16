package com.corkili.learningserver.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.protobuf.ByteString;

import com.corkili.learningserver.common.ControllerUtils;
import com.corkili.learningserver.common.ImageUtils;
import com.corkili.learningserver.common.ServiceUtils;
import com.corkili.learningserver.generate.protobuf.Info.Image;
import com.corkili.learningserver.generate.protobuf.Request.ImageLoadRequest;
import com.corkili.learningserver.generate.protobuf.Response.BaseResponse;
import com.corkili.learningserver.generate.protobuf.Response.ImageLoadResponse;
import com.corkili.learningserver.token.TokenManager;

@Controller
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private TokenManager tokenManager;

    @ResponseBody
    @RequestMapping(value = "/load", produces = "application/x-protobuf", method = RequestMethod.POST)
    public ImageLoadResponse loadImage(@RequestBody ImageLoadRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return ImageLoadResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        List<Image> imageList = new LinkedList<>();
        StringBuilder errPath = new StringBuilder();
        int i = 0;
        for (String imagePath : request.getImagePathList()) {
            byte[] image = ImageUtils.readImage(imagePath);
            boolean hasData = true;
            if (image == null || image.length == 0) {
                errPath.append(imagePath);
                if (++i != request.getImagePathCount()) {
                    errPath.append(",");
                }
                hasData = false;
            }
            imageList.add(Image.newBuilder()
                    .setPath(imagePath)
                    .setHasData(hasData)
                    .setImage(hasData ? ByteString.copyFrom(image) : ByteString.EMPTY)
                    .build());
        }
        String msg = "load image success";
        if (errPath.length() != 0) {
            msg = ServiceUtils.format("some image [{}] load failed", errPath.toString());
        }
        return ImageLoadResponse.newBuilder()
                .setResponse(ControllerUtils.generateSuccessBaseResponse(token, msg))
                .addAllImage(imageList)
                .build();
    }

}
