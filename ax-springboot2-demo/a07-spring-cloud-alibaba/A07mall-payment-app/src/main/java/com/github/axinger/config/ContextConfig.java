package com.github.axinger.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ContextConfig {

    @Bean
    @LoadBalanced // ribbon 负载均衡必须要
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

}
