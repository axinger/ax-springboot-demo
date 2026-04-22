package com.axing.common.http.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HttpConfig {

    @Bean
    public ClientHttpRequestFactory httpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        /// ms
        factory.setReadTimeout(30 * 1000);
        /// ms
        factory.setConnectTimeout(30 * 1000);
        return factory;
    }


    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }

//    @Bean
//    public WebClient webClient() {
////        return WebClient.create();
//        return   WebClient.builder().build();
//    }

}
