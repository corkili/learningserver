package com.corkili.learningserver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

import com.google.protobuf.GeneratedMessageV3;

import com.corkili.learningserver.generate.protobuf.Info.UserInfo;
import com.corkili.learningserver.generate.protobuf.Info.UserType;
import com.corkili.learningserver.generate.protobuf.Request.BaseRequest;
import com.corkili.learningserver.generate.protobuf.Request.UserLoginRequest;
import com.corkili.learningserver.generate.protobuf.Request.UserLogoutRequest;
import com.corkili.learningserver.generate.protobuf.Request.UserRegisterRequest;
import com.corkili.learningserver.generate.protobuf.Request.UserUpdateInfoRequest;
import com.corkili.learningserver.generate.protobuf.Response.UserLoginResponse;
import com.corkili.learningserver.generate.protobuf.Response.UserLogoutResponse;
import com.corkili.learningserver.generate.protobuf.Response.UserRegisterResponse;
import com.corkili.learningserver.generate.protobuf.Response.UserUpdateInfoResponse;


public class WebTests {

    private static final String token = "";

    @Test
    public void testRegister() {
        try {
            URI uri = new URI("http", null, "127.0.0.1", 8080, "/user/register", "", null);
            HttpPost httpRequest = new HttpPost(uri);
            BaseRequest baseRequest = BaseRequest.newBuilder().setToken(token).build();
            UserInfo userInfo = UserInfo.newBuilder()
                    .setPhone("18681255793")
                    .setUsername("corkili")
                    .setUserType(UserType.Teacher)
                    .build();
            UserRegisterRequest request = UserRegisterRequest.newBuilder()
                    .setRequest(baseRequest)
                    .setUserInfo(userInfo)
                    .setPassword("123456")
                    .build();
            HttpResponse httpResponse = doPost(httpRequest, request);
            UserRegisterResponse response = UserRegisterResponse.parseFrom(httpResponse.getEntity().getContent());
            System.out.println(request.toString());
            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLogin() {
        try {
            URI uri = new URI("http", null, "127.0.0.1", 8080, "/user/login", "", null);
            HttpPost httpRequest = new HttpPost(uri);
            BaseRequest baseRequest = BaseRequest.newBuilder().setToken(token).build();
            UserLoginRequest request = UserLoginRequest.newBuilder()
                    .setRequest(baseRequest)
                    .setPhone("15528235793")
                    .setPassword("654321")
                    .setUserType(UserType.Teacher)
                    .build();
            HttpResponse httpResponse = doPost(httpRequest, request);
            UserLoginResponse response = UserLoginResponse.parseFrom(httpResponse.getEntity().getContent());
            System.out.println(request.toString());
            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLogout() {
        try {
            URI uri = new URI("http", null, "127.0.0.1", 8080, "/user/logout", "", null);
            HttpPost httpRequest = new HttpPost(uri);
            BaseRequest baseRequest = BaseRequest.newBuilder().setToken(token).build();
            UserLogoutRequest request = UserLogoutRequest.newBuilder()
                    .setRequest(baseRequest)
                    .build();
            HttpResponse httpResponse = doPost(httpRequest, request);
            UserLogoutResponse response = UserLogoutResponse.parseFrom(httpResponse.getEntity().getContent());
            System.out.println(request.toString());
            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateInfo() {
        try {
            URI uri = new URI("http", null, "127.0.0.1", 8080, "/user/updateInfo", "", null);
            HttpPost httpRequest = new HttpPost(uri);
            BaseRequest baseRequest = BaseRequest.newBuilder().setToken(token).build();
            UserUpdateInfoRequest request = UserUpdateInfoRequest.newBuilder()
                    .setRequest(baseRequest)
                    .setPhone("15528235793")
                    .setUserType(UserType.Teacher)
                    .setUpdatePhone(false)
                    .setNewPhone("18681255793")
                    .setUpdateUsername(false)
                    .setNewUsername("corki")
                    .setUpdatePassword(true)
                    .setNewPassword("654321")
                    .build();
            HttpResponse httpResponse = doPost(httpRequest, request);
            UserUpdateInfoResponse response = UserUpdateInfoResponse.parseFrom(httpResponse.getEntity().getContent());
            System.out.println(request.toString());
            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static HttpResponse doPost(HttpPost post, GeneratedMessageV3 message) throws IOException {
        HttpClient httpclient = HttpClients.createDefault();
//        String requestUrl = post.getURI().toString();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(message.toByteArray());
        InputStreamEntity inputStreamEntity = new InputStreamEntity(inputStream);
        post.setEntity(inputStreamEntity);

        post.addHeader("Content-Type", "application/x-protobuf");
//        for (Header header : post.getAllHeaders()) {
//            System.out.println(header.getName() + ":" + header.getValue());
//        }
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("curl -XPOST ");
//        for (Header header : post.getAllHeaders()) {
//            sb.append(" -H \"").append(header.getName()).append(":").append(header.getValue()).append("\"");
//        }
//
//        String jsonBody = JsonFormat.printToString(message);
//        jsonBody = jsonBody.replace("\"", "\\\"");
//        sb.append(" -d \"").append(jsonBody).append("\"");
//        sb.append(" ").append(requestUrl);
//
//        System.out.println(sb.toString());
        return httpclient.execute(post);
    }

}
