package com.github.axinger.controller;

import brave.Tracer;
import brave.propagation.TraceContext;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequestMapping("/refund")
@RestController
public class RefundController {

    @Autowired
    private Tracer tracer;


    @Operation(summary = "支付系统,退款")
    @GetMapping(value = "/count")
    public Map<String, Object> payment(@RequestParam("id") String id) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("payment_id", id);
        map.put("payment_name", "支付系统,退款");
//        map.put("token", token);

        TraceContext context = tracer.currentSpan().start().context();

        map.put("parentId", context.parentId());
        map.put("traceId", context.traceId());
        map.put("spanId", context.spanId());

        map.put("traceIdHigh", context.traceIdHigh());
        map.put("traceIdString", context.traceIdString());
        map.put("isLocalRoot", context.isLocalRoot());
        return map;
    }

    @Operation(summary = "支付系统,退款2")
    @GetMapping(value = "/count2")
    public Map<String, Object> count2(@RequestParam("id") String id) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("payment_id", id);
        map.put("payment_name", "支付系统,退款2");
//        map.put("token", token);

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
