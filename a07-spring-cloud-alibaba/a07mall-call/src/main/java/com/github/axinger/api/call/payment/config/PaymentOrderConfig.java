package com.github.axinger.api.call.payment.config;

import cn.hutool.core.util.ObjUtil;
import com.github.axinger.api.TestServer;
import com.github.axinger.api.call.payment.CallApiUrl;
import feign.Logger;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.Collection;
import java.util.Map;


@Slf4j
public class PaymentOrderConfig {

    private final String contextPath = "/payment";

    @Value("${server-name.payment-service}")
    private String paymentService;


    @Autowired
    private TestServer testServer;

    @Bean
//    @RefreshScope
    public RequestInterceptor paymentOrderInterceptor(CallApiUrl callApiUrl) {
        return template -> {
            String url = template.url();
            String path = template.path();

            log.info("openfeign拦截器:url={},path={}", url, path);
            log.info("openfeign拦截器:paymentService={}", paymentService);
            System.out.println("testServer = " + testServer);

            Map<String, Collection<String>> headers = template.headers();
            Collection<String> authorization = headers.get("Authorization");
            log.info("openfeign拦截器:authorization={}", authorization);
            if (ObjUtil.isNotEmpty(authorization)) {
                template.removeHeader("Authorization");
                for (String string : authorization) {
                    if (!string.startsWith("Bearer ")) {
                        template.header("Authorization", "Bearer " + string);
                    }
                }
            }
//            String url = template.url();
//            String originalPath = template.path();
//            template.uri(contextPath + url + originalPath);
        };
    }

    @Bean
    Logger.Level paymentOrderLoggerLevel() {
        return Logger.Level.BASIC;
    }

    public static class PaymentRefundConfig {


        private final String contextPath = "/payment";

        @Value("${server-name.payment-service}")
        private String paymentService;

        @Autowired
        private TestServer testServer;

        @Bean
        public RequestInterceptor paymentRefundInterceptor() {
            return template -> {
                System.out.println("paymentService = " + paymentService);

                System.out.println("testServer = " + testServer);


                //            template.header("Authorization", "Bearer " + "Refund");
                //            String url = template.url();
                //            String originalPath = template.path();
                //            template.uri(contextPath+url + originalPath);

            };
        }

        @Bean
        Logger.Level paymentRefundLoggerLevel() {
            return Logger.Level.BASIC;
        }
    }
}
