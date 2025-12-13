package com.github.axinger.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "订单测试页面", description = "TestController")
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Operation(summary = "测试1")
    @GetMapping(value = "/1")
    public Object test1(@RequestHeader HttpHeaders headers) {
        log.info("test1,查看网关请求头,headers={}", headers);
        Map<String, Object> map = new HashMap<>(16);
        map.put("name", "测试接口1");
        map.put("date", LocalDateTime.now());
        return map;
    }

    @Operation(summary = "测试2")
    @GetMapping(value = "/2")
    public Object test2(@RequestHeader HttpHeaders headers) {
        log.info("test2,查看网关请求头,headers={}", headers);
        Map<String, Object> map = new HashMap<>(16);
        map.put("name", "测试接口2");
        map.put("date", LocalDateTime.now());
        return map;
    }
}
