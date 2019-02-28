package com.corkili.learningserver.controller;

import com.corkili.learningserver.bo.Message;
import com.corkili.learningserver.common.ControllerUtils;
import com.corkili.learningserver.common.ImageUtils;
import com.corkili.learningserver.common.ProtoUtils;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.generate.protobuf.Info.MessageInfo;
import com.corkili.learningserver.generate.protobuf.Request.MessageCreateRequest;
import com.corkili.learningserver.generate.protobuf.Request.MessageDeleteRequest;
import com.corkili.learningserver.generate.protobuf.Request.MessageFindAllRequest;
import com.corkili.learningserver.generate.protobuf.Response.BaseResponse;
import com.corkili.learningserver.generate.protobuf.Response.MessageCreateResponse;
import com.corkili.learningserver.generate.protobuf.Response.MessageDeleteResponse;
import com.corkili.learningserver.generate.protobuf.Response.MessageFindAllResponse;
import com.corkili.learningserver.service.MessageService;
import com.corkili.learningserver.service.UserService;
import com.corkili.learningserver.token.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/create", produces = "application/x-protobuf", method = RequestMethod.POST)
    public MessageCreateResponse createMessage(@RequestBody MessageCreateRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return MessageCreateResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        Long userId = tokenManager.getUserIdAssociatedWithToken(token);
        Message message = new Message();
        byte[] imageData = new byte[0];
        if (request.hasImage()) {
            message.setContent(ImageUtils.getImagePath("message", request.getImage().getPath(), userId), true);
            imageData = request.getImage().getImage().toByteArray();
        } else {
            message.setContent(request.getText(), false);
        }
        message.setReceiverId(request.getReceiverId());
        message.setSenderId(request.getSenderId());
        ServiceResult serviceResult = messageService.createMessage(message, imageData);
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        MessageInfo messageInfo;
        if (serviceResult.isSuccess()) {
            message = serviceResult.extra(Message.class);
            messageInfo = ProtoUtils.generateMessageInfo(message,
                    userService.retrieve(message.getReceiverId()).orElse(null),
                    userService.retrieve(message.getSenderId()).orElse(null), true);
        } else {
            messageInfo = ProtoUtils.generateMessageInfo(message, null, null, false);
        }
        return MessageCreateResponse.newBuilder()
                .setResponse(baseResponse)
                .setMessageInfo(messageInfo)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/findAll", produces = "application/x-protobuf", method = RequestMethod.POST)
    public MessageFindAllResponse findAllMessage(@RequestBody MessageFindAllRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return MessageFindAllResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        ServiceResult serviceResult = messageService.findAllMessage(
                request.getByReceiverId() ? request.getReceiverId() : null,
                request.getBySenderId() ? request.getSenderId() : null, request.getReverse());
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        List<MessageInfo> messageInfos = new LinkedList<>();
        for (Object o : serviceResult.extra(List.class)) {
            Message message = (Message) o;
            messageInfos.add(ProtoUtils.generateMessageInfo(message,
                    userService.retrieve(message.getReceiverId()).orElse(null),
                    userService.retrieve(message.getSenderId()).orElse(null), true));
        }
        return MessageFindAllResponse.newBuilder()
                .setResponse(baseResponse)
                .addAllMessageInfo(messageInfos)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/delete", produces = "application/x-protobuf", method = RequestMethod.POST)
    public MessageDeleteResponse deleteMessage(@RequestBody MessageDeleteRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return MessageDeleteResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        ServiceResult serviceResult = messageService.deleteMessage(request.getMessageId());
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        return MessageDeleteResponse.newBuilder()
                .setResponse(baseResponse)
                .setMessageId(request.getMessageId())
                .build();
    }
}
