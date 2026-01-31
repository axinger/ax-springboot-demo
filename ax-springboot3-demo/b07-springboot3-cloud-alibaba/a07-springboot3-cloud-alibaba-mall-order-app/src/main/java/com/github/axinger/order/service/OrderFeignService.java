package com.github.axinger.order.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Component
@FeignClient(value = "${server-name.payment-service}", path = "/payment")
//@FeignClient(value = "demo07-nacos-payment", path = "/payment")
public interface OrderFeignService {

    @GetMapping(value = "/count/{id}")
    Map<String, Object> count(@PathVariable("id") Integer id);
}
