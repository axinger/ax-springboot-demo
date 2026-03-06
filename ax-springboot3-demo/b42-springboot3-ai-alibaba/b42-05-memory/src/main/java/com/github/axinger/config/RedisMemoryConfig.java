package com.github.axinger.config;

import com.alibaba.cloud.ai.memory.redis.JedisRedisChatMemoryRepository;
import com.alibaba.cloud.ai.memory.redis.LettuceRedisChatMemoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisMemoryConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.database}")
    private int database;

    @Bean
    public JedisRedisChatMemoryRepository jedisRedisChatMemoryRepository() {
        return JedisRedisChatMemoryRepository.builder()
                .host(redisHost)
                .port(redisPort)
                .database(database)
                .keyPrefix("spring-ai:alibaba_chat_memory:")
                .timeout(5000)
                .build();
    }

    @Bean
    public LettuceRedisChatMemoryRepository lettuceRedisChatMemoryRepository() {
        return LettuceRedisChatMemoryRepository.builder()
                .host(redisHost)
                .port(redisPort)
                .database(database)
                .keyPrefix("spring-ai:alibaba_chat_memory:")
                .timeout(5000)
                .build();
    }

//    @Bean
//    public RedissonRedisChatMemoryRepository redissonRedisChatMemoryRepository() {
//        return RedissonRedisChatMemoryRepository.builder()
//                .host(redisHost)
//                .port(redisPort)
//                .build();
//    }
}
