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
import com.corkili.learningserver.generate.protobuf.Request.UserRegisterRequest;
import com.corkili.learningserver.generate.protobuf.Response.UserLoginResponse;
import com.corkili.learningserver.generate.protobuf.Response.UserRegisterResponse;


public class WebTests {

    private static final String token = "710b35e4-5dc1-42ca-8b37-63cbc86ecc88";

    @Test
    public void testRegister() {
        try {
            URI uri = new URI("http", null, "127.0.0.1", 8080, "/user/register", "", null);
            HttpPost request = new HttpPost(uri);
            BaseRequest baseRequest = BaseRequest.newBuilder().setToken(token).build();
            UserInfo userInfo = UserInfo.newBuilder()
                    .setPhone("18681255793")
                    .setUsername("corkili")
                    .setUserType(UserType.Teacher)
                    .build();
            UserRegisterRequest userRegisterRequest = UserRegisterRequest.newBuilder()
                    .setRequest(baseRequest)
                    .setUserInfo(userInfo)
                    .setPassword("123456")
                    .build();
            HttpResponse response = doPost(request, userRegisterRequest);
            UserRegisterResponse userRegisterResponse = UserRegisterResponse.parseFrom(response.getEntity().getContent());
            System.out.println(userRegisterRequest.toString());
            System.out.println(userRegisterResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLogin() {
        try {
            URI uri = new URI("http", null, "127.0.0.1", 8080, "/user/login", "", null);
            HttpPost request = new HttpPost(uri);
            BaseRequest baseRequest = BaseRequest.newBuilder().setToken(token).build();
            UserLoginRequest userLoginRequest = UserLoginRequest.newBuilder()
                    .setRequest(baseRequest)
                    .setPhone("18681255793")
                    .setPassword("123456")
                    .setUserType(UserType.Teacher)
                    .build();
            HttpResponse response = doPost(request, userLoginRequest);
            UserLoginResponse userLoginResponse = UserLoginResponse.parseFrom(response.getEntity().getContent());
            System.out.println(userLoginRequest.toString());
            System.out.println(userLoginResponse.toString());
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
