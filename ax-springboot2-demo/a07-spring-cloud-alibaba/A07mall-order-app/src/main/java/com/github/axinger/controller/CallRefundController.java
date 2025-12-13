package com.github.axinger.controller;

import com.github.axinger.api.call.payment.PaymentRefundCall;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xing
 */
@RefreshScope
@RestController
@Slf4j
@RequestMapping("/call")
public class CallRefundController {


    @Autowired
    private PaymentRefundCall paymentOrderApi;

    @Operation(summary = "订单系统,调用退货订单")
    @GetMapping(value = "/refund")
    public Object refund(String id) {
        return paymentOrderApi.refund(id);
    }


}
