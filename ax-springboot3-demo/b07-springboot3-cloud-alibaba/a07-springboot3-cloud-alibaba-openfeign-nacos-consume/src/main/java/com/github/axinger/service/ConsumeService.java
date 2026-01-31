package com.github.axinger.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@FeignClient(value = "openfeign-nacos-provider", path = "/mes")
public interface ConsumeService {

    @GetMapping(value = "/test1")
    Object test();

}
