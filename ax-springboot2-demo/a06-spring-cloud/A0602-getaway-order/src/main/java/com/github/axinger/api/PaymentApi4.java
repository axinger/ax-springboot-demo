package com.github.axinger.api;

import com.github.axinger.config.PaymentApi4Config;
import com.github.axinger.dto.PaymentApiDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(name = "PaymentApi4", url = "${server-url.payment-url}",
        path = "/payment4",
        configuration = PaymentApi4Config.class)
public interface PaymentApi4 {

    @PostMapping(value = "/login")
    PaymentApiDTO.LoginVO login(@RequestBody PaymentApiDTO.LoginDTO dto);


    @GetMapping(value = "/payment")
    PaymentApiDTO.PaymentVO payment(@SpringQueryMap PaymentApiDTO.PaymentDTO dto);

}
