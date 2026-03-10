package com.github.axinger.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;


@Configuration
public class RedisConfig {

    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        // 序列号key value
        RedisSerializer<String> key = RedisSerializer.string();
        RedisSerializer<Object> value = new GenericJackson2JsonRedisSerializer();

        redisTemplate.setKeySerializer(key);
        redisTemplate.setValueSerializer(value);
        // hash
        redisTemplate.setHashKeySerializer(key);
        redisTemplate.setHashValueSerializer(value);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


}
