package com.github.axinger.api.call.payment;

import com.github.axinger.api.call.payment.config.PaymentOrderConfig;
import com.github.axinger.api.call.payment.config.PaymentRefundCallConfig;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@RefreshScope
@Component
@FeignClient(
        value = "PaymentOrderApi2",
        contextId = "PaymentOrderApi2",
        url = "${callApi.payment.refund.url}",
        configuration = PaymentRefundCallConfig.class)
@Import(PaymentOrderConfig.class)
public interface PaymentRefundCall {

    @GetMapping
    Map<String, Object> refund(@RequestParam("id") String id);
}
