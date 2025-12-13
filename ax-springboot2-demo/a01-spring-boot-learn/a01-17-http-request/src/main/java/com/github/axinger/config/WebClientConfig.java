package com.github.axinger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class WebClientConfig {

//    @Bean
//    public WebClient.Builder webClientBuilder() {
//        return WebClient.builder();
//    }
//
//    @Bean
//    public WebClient.Builder loadBalancedWebClientBuilder(ExchangeFilterFunction loadBalancer) {
//        return WebClient.builder().filter(loadBalancer);
//    }

    @Bean
    public WebClient webClient() {

//        WebClient webClient = WebClient.create();

//        WebClient restClient = WebClient.builder()
//                .baseUrl(properties.getUrl())
//                .defaultHeader(HttpHeaders.AUTHORIZATION,
//                        encodeBasic("pig", "pig")
//                ).build();


        return WebClient.builder()
//                .baseUrl("http://localhost:8080")
                .defaultHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko)")
                .defaultHeader("token", "test_token")
                .build();

    }

}
