package com.github.axinger.controller;

import com.github.axinger.api.PaymentApi;
import com.github.axinger.api.PaymentApi2;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xing
 */
@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {


    @Autowired
    private PaymentApi paymentApi;
    @Autowired
    private PaymentApi2 paymentApi2;

    @Operation(summary = "直接调用请求支付系统")
    @GetMapping(value = "/test")
    public Object order1() {
        Map<String, String> map = new HashMap<>();
        map.put("orderId", "1");

        Map<String, Object> abc123 = paymentApi.test1("abc123", map);

        return abc123;
    }

    @GetMapping(value = "/test2")
    public Object test2() {
        Map<String, String> map = new HashMap<>();
        map.put("orderId", "1");

        Map<String, Object> abc123 = paymentApi2.test1(map);

        return abc123;
    }

}
