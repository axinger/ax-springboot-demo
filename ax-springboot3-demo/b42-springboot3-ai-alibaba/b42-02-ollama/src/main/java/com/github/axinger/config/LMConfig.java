package com.github.axinger.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LMConfig {

    @Value("${spring.ai.dashscope.api-key}")
    private String key;

//    @Bean
//    public DashScopeApi dashScopeApi() {
//        return DashScopeApi.builder()
//                .apiKey(key)
//                .build();
//    }

//    @Bean
//    @Primary
//    public OllamaChatModel ollamaChatModel() {
//        return OllamaChatModel.builder()
//                .build();
//    }
}
