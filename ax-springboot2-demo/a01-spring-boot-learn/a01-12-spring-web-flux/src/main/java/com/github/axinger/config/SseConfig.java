package com.github.axinger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class SseConfig {

    @Bean
    public Sinks.Many<String> sseSink() {
        return Sinks.many().multicast().onBackpressureBuffer();
    }
}
