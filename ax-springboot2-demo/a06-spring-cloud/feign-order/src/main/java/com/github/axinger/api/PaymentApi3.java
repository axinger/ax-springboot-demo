package com.github.axinger.api;

import com.github.axinger.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Component
@FeignClient(name = "PaymentApi2", url = "${server-url.payment-url}", path = "/payment", configuration = FeignClientConfig.class)
public interface PaymentApi3 {

    @PostMapping(value = "/test2")
    Map<String, Object> test1(@RequestBody Map<String, String> map);

}
