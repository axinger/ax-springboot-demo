package com.github.axinger.controller;

import brave.Tracer;
import brave.propagation.TraceContext;
import com.axing.common.response.dto.Result;
import com.github.axinger.api.call.payment.PaymentCall;
import com.github.axinger.api.call.payment.RefundCall;
import com.github.axinger.api.dto.PaymentDTO;
import com.github.axinger.api.dto.PaymentVO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/useOpenfeign")
public class UseOpenfeignController {

    @Autowired
    private PaymentCall paymentOrderApi;

    @Autowired
    private RefundCall paymentRefundApi;

    @Autowired
    private Tracer tracer;

    @Value("${server.port}")
    private String port;

    @Resource
    private RestTemplate restTemplate;


    @Value("${server-url.nacos-payment-service}")
    private String paymentURL;

    @Value("${server-url.gateway-service}")
    private String gatewayService;

    @Operation(summary = "open方式请求,请求支付系统", description = "gatewayFeign")
    @GetMapping(value = "/test1/{id}")
    public Object test1(@PathVariable("id") Integer id) {
        Result<PaymentVO> result = paymentOrderApi.payment1("abcd", PaymentDTO.builder().id(id.toString()).build());
//        Map<String, Object> res = new HashMap<>(16);
//        res.put("order", getRes(id));
//        res.put("payment", map2);
        return result;
    }


    @Operation(summary = "open方式请求,请求支付系统2", description = "gatewayFeign")
    @GetMapping(value = "/test2/{id}")
    public Object test2(@PathVariable("id") Integer id) {
        Result<PaymentVO> result = paymentOrderApi.payment2("abcd", PaymentDTO.builder().id(id.toString()).build());
//        Map<String, Object> res = new HashMap<>(16);
//        res.put("order", getRes(id));
//        res.put("payment", map2);
        return result;
    }


    @Operation(summary = "open方式请求,请求退款", description = "gatewayFeign")
    @GetMapping(value = "/test3/{id}")
    public Object test3(@PathVariable("id") Integer id) {
        Map<String, Object> map2 = paymentRefundApi.count(id);
        Map<String, Object> res = new HashMap<>(16);
        res.put("order", getRes(id));
        res.put("payment", map2);
        return res;
    }

    public Map<String, Object> getRes(Integer id) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("order_id", id);
        map.put("order_name", "订单");
        map.put("order_port", port);
        TraceContext context = tracer.currentSpan().start().context();
        map.put("parentId", context.parentId());
        map.put("traceId", context.traceId());
        map.put("spanId", context.spanId());
        map.put("traceIdHigh", context.traceIdHigh());
        map.put("traceIdString", context.traceIdString());
        map.put("isLocalRoot", context.isLocalRoot());
        return map;
    }
}
