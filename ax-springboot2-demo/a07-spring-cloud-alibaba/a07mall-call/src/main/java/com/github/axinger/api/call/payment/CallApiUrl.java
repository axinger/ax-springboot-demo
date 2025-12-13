package com.github.axinger.api.call.payment;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "call-api.payment")
@RefreshScope
public class CallApiUrl {

    private RefundItem refund;

    @Data
    public static class RefundItem {

        private String url;

    }

}
