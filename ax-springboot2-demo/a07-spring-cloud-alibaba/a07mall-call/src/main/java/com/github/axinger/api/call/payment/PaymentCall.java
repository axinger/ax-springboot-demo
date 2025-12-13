package com.github.axinger.api.call.payment;

import com.axing.common.response.dto.Result;
import com.github.axinger.api.call.payment.config.PaymentOrderConfig;
import com.github.axinger.api.dto.PaymentDTO;
import com.github.axinger.api.dto.PaymentVO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

// @FeignClient configuration 指定的配置类不要加@Configuration，如果加了@Configuration注解，那这个配置类就是一个全局配置类。
@Component
@FeignClient(value = "${server-name.payment-service}/payment",
        path = "/order",
        contextId = "PaymentOrderApi",
        configuration = PaymentOrderConfig.class)
@Import(PaymentOrderConfig.class)
public interface PaymentCall {

    @Operation(summary = "支付系统,订单,payment1")
    /// get请求分组dto参数
    @GetMapping(value = "/payment1")
    Result<PaymentVO> payment1(@RequestHeader("Authorization") String token, @SpringQueryMap PaymentDTO dto);


    @Operation(summary = "支付系统,订单,payment2")
    @GetMapping(value = "/payment2")
    Result<PaymentVO> payment2(@RequestHeader("Authorization") String token, @RequestBody PaymentDTO dto);
}
