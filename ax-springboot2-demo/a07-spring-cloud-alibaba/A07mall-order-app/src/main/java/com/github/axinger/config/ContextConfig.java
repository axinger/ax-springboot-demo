package com.github.axinger.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName ContextConfig.java
 * @Description TODO
 * @createTime 2021年12月16日 19:26:00
 */
@Configuration
public class ContextConfig {

    @Bean
    @LoadBalanced // ribbon 负载均衡必须要
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
