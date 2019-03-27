package com.corkili.learningserver.controller;

import com.corkili.learningserver.bo.CourseCatalog;
import com.corkili.learningserver.common.ControllerUtils;
import com.corkili.learningserver.common.ProtoUtils;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.generate.protobuf.Request.CourseCatalogQueryRequest;
import com.corkili.learningserver.generate.protobuf.Request.NavigationProcessRequest;
import com.corkili.learningserver.generate.protobuf.Response.BaseResponse;
import com.corkili.learningserver.generate.protobuf.Response.CourseCatalogQueryResponse;
import com.corkili.learningserver.generate.protobuf.Response.NavigationProcessResponse;
import com.corkili.learningserver.scorm.cam.model.DeliveryContent;
import com.corkili.learningserver.scorm.sn.api.event.EventType;
import com.corkili.learningserver.scorm.sn.api.event.NavigationEvent;
import com.corkili.learningserver.service.ScormService;
import com.corkili.learningserver.service.UserService;
import com.corkili.learningserver.token.TokenManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/scorm")
public class ScormController {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private ScormService scormService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/queryCatalog", produces = "application/x-protobuf", method = RequestMethod.POST)
    public CourseCatalogQueryResponse queryCatalog(@RequestBody CourseCatalogQueryRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return CourseCatalogQueryResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        ServiceResult serviceResult = scormService.queryCatalog(request.getScormId());
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        return CourseCatalogQueryResponse.newBuilder()
                .setResponse(baseResponse)
                .setCourseCatalogInfo(ProtoUtils.generateCourseCatalogInfo(serviceResult.extra(CourseCatalog.class)))
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/processNavigation", produces = "application/x-protobuf", method = RequestMethod.POST)
    public NavigationProcessResponse processNavigation(@RequestBody NavigationProcessRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return NavigationProcessResponse.newBuilder()
                    .setResponse(baseResponse)
                    .setNavigationEventType(request.getNavigationEventType())
                    .setTargetItemId(request.getTargetItemId())
                    .setScormId(request.getScormId())
                    .setLevel1CatalogItemId(request.getLevel1CatalogItemId())
                    .build();
        }
        ServiceResult serviceResult = scormService.processNavigationEvent(
                new NavigationEvent(EventType.valueOf(request.getNavigationEventType().name()),
                        StringUtils.isBlank(request.getTargetItemId()) ? null : request.getTargetItemId()),
                tokenManager.getUserIdAssociatedWithToken(token), request.getScormId(), request.getLevel1CatalogItemId());
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        return NavigationProcessResponse.newBuilder()
                .setResponse(baseResponse)
                .setNavigationEventType(request.getNavigationEventType())
                .setTargetItemId(request.getTargetItemId())
                .setScormId(request.getScormId())
                .setLevel1CatalogItemId(request.getLevel1CatalogItemId())
                .setDeliveryContentInfo(ProtoUtils.generateDeliveryContentInfo(serviceResult.extra(DeliveryContent.class)))
                .build();
    }
}
