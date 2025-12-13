package com.github.axinger.controller;


import brave.Tracer;
import brave.propagation.TraceContext;
import com.axing.common.response.dto.Result;
import com.github.axinger.api.dto.PaymentDTO;
import com.github.axinger.api.dto.PaymentVO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xing
 */
@RestController
@RequestMapping("/order")
public class PaymentOrderController {

    @Value("${server.port}")
    private String port;

    @Autowired
    private Tracer tracer;


    @Operation(summary = "支付系统,订单")
    /// get请求分组dto参数
    @GetMapping(value = "/payment1")
    public Result<PaymentVO> payment1(@RequestHeader("Authorization") String token, @SpringQueryMap PaymentDTO dto) {
        String id = dto.getId();
        System.out.println("payment_id = " + id);
        Map<String, Object> map = new HashMap<>(16);
        map.put("payment_id", id);
        map.put("payment_name", "支付系统");
        map.put("payment_port", port);
        map.put("token", token);

        TraceContext context = tracer.currentSpan().start().context();

        map.put("parentId", context.parentId());
        map.put("traceId", context.traceId());
        map.put("spanId", context.spanId());

        map.put("traceIdHigh", context.traceIdHigh());
        map.put("traceIdString", context.traceIdString());
        map.put("isLocalRoot", context.isLocalRoot());

        Result<PaymentVO> result = Result.success(PaymentVO.builder()
                .id(id)
                .build());
        result.setParams(map);
        return result;
    }

    @Operation(summary = "支付系统,订单2")
    @PostMapping(value = "/payment2")
    public Result<PaymentVO> payment2(@RequestHeader("Authorization") String token, @RequestBody PaymentDTO dto) {
        String id = dto.getId();
        Result<PaymentVO> result = Result.success(PaymentVO.builder()
                .id(id)
                .build());

        result.setParams(Map.of("payment_id", id, "token", token));
        return result;
    }

}
