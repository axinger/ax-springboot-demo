package com.github.axinger.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.axing.common.response.dto.Result;
import com.github.axinger.server.UserServiceHttp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName IndexController.java
 * @description TODO
 * @createTime 2022年07月27日 21:02:00
 */

@RestController
public class IndexController {

    @Autowired
    private UserServiceHttp userServiceHttp;

    @PostMapping("/data1")
    public Object data1(@RequestBody Map map) {
        Map<String, Object> resultMap = new HashMap();
        resultMap.put("date", new Date());
        resultMap.putAll(map);
        return resultMap;
    }

    @GetMapping("/http")
    public void http() {
        Result<Map<String, Object>> useToken = userServiceHttp.getUseToken("jim", "123", "abcv");
        System.out.println("useToken = " + useToken);
    }


    @GetMapping("/getUserToken")
    public Result<?> data1(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestHeader("token") String token) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("date", new Date());
        resultMap.put("username", username);
        resultMap.put("password", password);
        resultMap.put(token, token);
        return Result.success(resultMap);
    }


    @GetMapping("/post")
    void test1() throws IOException, InterruptedException {
        Map map = new HashMap();
        map.put("name", "jim");

        String data = JSON.toJSONString(map);// json 请求数据
//        HttpRequest request = HttpRequest.newBuilder(URI.create("http://127.0.0.1:8080/data1"))
//                .POST(HttpRequest.BodyPublishers.ofString(data))
//                .header("Content-Type", "application/json") //设置头部信息
//                .timeout(Duration.ofSeconds(10)) // 设置响应超时时间
//                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .header("Content-Type", "application/json") // 设置头部信息
                .uri(URI.create("http://127.0.0.1:8080/data1"))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());


        System.out.println(response.body());
        final JSONObject jsonObject = JSON.parseObject(response.body());


        System.out.println("JSON.parseObject(response.body()) = " + jsonObject + " = " + jsonObject.get("name"));
    }
}
