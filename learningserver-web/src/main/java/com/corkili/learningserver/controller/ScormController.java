package com.corkili.learningserver.controller;

import com.alibaba.fastjson.JSONObject;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

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

    @ResponseBody
    @RequestMapping(value = "/{token}/{scormId}/{itemId}/lmsRuntimeAPI", method = RequestMethod.POST)
    public String lmsRuntimeAPI(HttpServletRequest request, @PathVariable("token") String token,
                                @PathVariable("scormId") Long scormId, @PathVariable("itemId") String itemId) {
        token = tokenManager.getOrNewToken(token);
        if (!tokenManager.isLogin(token)) {
            JSONObject response = new JSONObject();
            response.put("result", "false");
            response.put("msg", "not login");
            response.put("returnValue", "");
            response.put("lastError", 0);
            return response.toString();
        }
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            JSONObject response = new JSONObject();
            response.put("result", "false");
            response.put("msg", "read request data failed");
            response.put("returnValue", "");
            response.put("lastError", 0);
            return response.toString();
        }
        JSONObject requestData = JSONObject.parseObject(sb.toString());
        ServiceResult serviceResult = scormService.invokeLMSRuntimeAPI(tokenManager.getUserIdAssociatedWithToken(token),
                scormId, itemId, requestData.getString("methodName"), requestData.getString("parameter1"), requestData.getString("parameter2"));
        String result;
        String msg;
        String returnValue;
        int lastError;
        if (serviceResult.isSuccess()) {
            result = "true";
            msg = serviceResult.msg();
            returnValue = serviceResult.extra(String.class);
            ServiceResult serviceResult2 = scormService.invokeLMSRuntimeAPI(tokenManager.getUserIdAssociatedWithToken(token),
                    scormId, itemId, "getLastError", null, null);
            if (serviceResult2.isSuccess()) {
                lastError = Integer.parseInt(serviceResult2.extra(String.class));
            } else {
                lastError = 0;
            }
        } else {
            result = "false";
            msg = serviceResult.msg();
            returnValue = "";
            lastError = 0;
        }
        JSONObject response = new JSONObject();
        response.put("result", result);
        response.put("msg", msg);
        response.put("returnValue", returnValue);
        response.put("lastError", lastError);
        return response.toString();
    }

}
