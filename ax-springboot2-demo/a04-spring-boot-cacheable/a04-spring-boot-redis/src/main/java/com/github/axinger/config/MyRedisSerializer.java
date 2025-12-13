package com.github.axinger.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

public interface MyRedisSerializer {

    static RedisSerializer<Object> jackson2JsonRedisSerializer() {

//        ObjectMapper objectMapper = new CommonObjectMapper(jsonProperties);
//
//        ObjectMapper objectMapper = ObjectMapperFactory.factory(jsonProperties);
//
//
////        // 将当前对象的数据类型也存入序列化的结果字符串中，以便反序列化
//        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
//
//        return new GenericJackson2JsonRedisSerializer(objectMapper);

//        return new FastJsonRedisSerializer<>(Object.class);

//        return new GenericToStringSerializer<>(Object.class);

        // 使用 Jackson2JsonRedisSerializer 序列化值
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        // 解决 LocalDateTime 序列化问题
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule()); // 注册 Java 8 时间模块
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // 禁用时间戳格式
        serializer.setObjectMapper(om);
        return serializer;

    }

    static RedisSerializer<Object> genericJackson2JsonRedisSerializer() {

//        ObjectMapper objectMapper = new CommonObjectMapper(jsonProperties);
//
//        ObjectMapper objectMapper = ObjectMapperFactory.factory(jsonProperties);
//
//
////        // 将当前对象的数据类型也存入序列化的结果字符串中，以便反序列化
//        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
//

//        return new FastJsonRedisSerializer<>(Object.class);

//        return new GenericToStringSerializer<>(Object.class);

        // 使用 Jackson2JsonRedisSerializer 序列化值
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        // 解决 LocalDateTime 序列化问题
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule()); // 注册 Java 8 时间模块
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // 禁用时间戳格式
        serializer.setObjectMapper(om);
        return new GenericJackson2JsonRedisSerializer(om);

    }
}
