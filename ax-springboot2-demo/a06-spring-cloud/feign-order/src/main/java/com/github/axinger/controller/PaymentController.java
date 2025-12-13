package com.github.axinger.controller;


import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xing
 */
@RequestMapping("/payment")
@RestController
public class PaymentController {

    @Value("${server.port}")
    private String port;


    @Operation(summary = "支付系统,订单")
    @PostMapping(value = "/test1")
    public Map<String, Object> test1(@RequestHeader("token") String token, @RequestBody Map<String, String> map) {
        Map<String, Object> map2 = new HashMap<>(16);
        map2.put("token", token);
        map2.put("name", "支付系统test1");
        map2.putAll(map);
        return map2;
    }

    @Operation(summary = "支付系统,订单")
    @PostMapping(value = "/test2")
    public Map<String, Object> test2(@RequestHeader("token") String token, @RequestBody Map<String, String> map) {
        Map<String, Object> map2 = new HashMap<>(16);
        map2.put("token", token);
        map2.put("name", "支付系统 test2");
        map2.putAll(map);
        return map2;
    }

}
