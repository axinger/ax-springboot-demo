package com.github.axinger.api;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@Component
@FeignClient(name = "PaymentApi", url = "${server-url.payment-url}", path = "/payment")
@Headers("Authorization: Bearer your-token") // 添加固定请求头
public interface PaymentApi {

    @PostMapping(value = "/test1")
    Map<String, Object> test1(@RequestHeader("token") String token, @RequestBody Map<String, String> map);

}
