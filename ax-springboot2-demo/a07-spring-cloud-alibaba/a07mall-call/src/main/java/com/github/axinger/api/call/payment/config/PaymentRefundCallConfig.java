package com.github.axinger.api.call.payment.config;

import com.github.axinger.api.call.payment.CallApiUrl;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

@Slf4j
public class PaymentRefundCallConfig {

    @Bean
    public RequestInterceptor paymentOrder2Interceptor(CallApiUrl callApiUrl) {
        return template -> {
            String refundUrl = callApiUrl.getRefund().getUrl();
            System.out.println("refundUrl = " + refundUrl);
            template.target(refundUrl);

            //            String url = template.url();
//            String originalPath = template.path();
//            template.uri(contextPath + url + originalPath);
        };
    }
}
