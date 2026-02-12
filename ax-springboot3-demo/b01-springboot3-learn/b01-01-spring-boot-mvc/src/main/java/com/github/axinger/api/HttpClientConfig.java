package com.github.axinger.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class HttpClientConfig {

//    @Bean
//    RestClient.Builder restClientBuilder() {
//        return RestClient.builder();
//    }
//
//    @Bean
//    public OrderApi helloClient(RestClient.Builder builder) {
//
//        RestClient restClient = builder.baseUrl("").build();
//
//        return HttpServiceProxyFactory.builder()
//                .exchangeAdapter(RestClientAdapter.create(restClient))
//                .build()
//                .createClient(OrderApi.class);
//    }

    @Bean
    public HttpServiceProxyFactory httpServiceProxyFactory() {
        RestClient client = RestClient.builder()
//                .baseUrl("")
                .build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder()
                .exchangeAdapter(RestClientAdapter.create(client))
                .build();
        return factory;
    }

    @Bean
    public OrderApi orderApi(HttpServiceProxyFactory factory) {
        return factory.createClient(OrderApi.class);
    }

}
