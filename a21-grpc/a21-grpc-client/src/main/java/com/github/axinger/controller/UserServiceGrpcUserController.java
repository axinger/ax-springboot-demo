package com.github.axinger.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.google.protobuf.util.JsonFormat;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.axinger.api.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Service
@Slf4j
@RestController
@RequestMapping("/user")
public class UserServiceGrpcUserController {


    @GrpcClient("a21-grpc-server")
    private UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;

    @SneakyThrows
    public Map<String, Object> test3() {
        // 构建请求
        List<UserOuterClass.User> list = new ArrayList<>() {{
            add(UserOuterClass.User.newBuilder().setName("张三").build());
            add(UserOuterClass.User.newBuilder().setName("李四").build());
        }};

        UserOuterClass.UserList userList = UserOuterClass.UserList.newBuilder().addAllData(list).build();

        // 发送请求并接收响应
        ResponseOuterClass.Response response = userServiceBlockingStub.getUserList(userList);

        // 处理响应
        Map<String, Object> map = new HashMap<>();
        StatusCodeOuterClass.StatusCode code = response.getCode();
        String message = response.getMessage();
        ResponsePayloadOuterClass.ResponsePayload payload = response.getPayload();

        // 检查 payload 类型，并安全地获取 UserList
        if (response.hasPayload() && payload.hasUserList()) {
            UserOuterClass.UserList userList1 = payload.getUserList();
            ///  这个不能直接返回前端,无法序列化
            List<UserOuterClass.User> usersList = userList1.getDataList();

            //protobuf-java-util
            String jsonString = JsonFormat.printer().print(userList);

            // 如果需要进一步处理 JSON 字符串，可以使用 Jackson ObjectMapper

            Map<String, Object> jsonMap = JSON.parseObject(jsonString, new TypeReference<>() {
            });
            System.out.println("jsonMap = " + jsonMap);


            map.put("code", code);
            map.put("message", message);
            map.putAll(jsonMap);
        } else {
            // 如果 payload 不是预期的类型，则处理错误情况
            map.put("code", code);
            map.put("message", message);
            map.put("error", "Unexpected payload type or empty payload");
        }
        return map;
    }
}
