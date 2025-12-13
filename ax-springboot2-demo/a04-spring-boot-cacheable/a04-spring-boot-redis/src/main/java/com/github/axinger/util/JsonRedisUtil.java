package com.github.axinger.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class JsonRedisUtil {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public JsonRedisUtil(@Qualifier("myStringRedisTemplate") RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * 存储对象为 JSON
     */
    public <T> void set(String key, T value) {
        set(key, value, null);
    }

    public <T> void set(String key, T value, Duration timeout) {
        if (key == null || value == null) return;

        try {
            String json = objectMapper.writeValueAsString(value);
            if (timeout != null) {
                redisTemplate.opsForValue().set(key, json, timeout);
            } else {
                redisTemplate.opsForValue().set(key, json);
            }
        } catch (Exception e) {
            log.error("Redis set failed for key: {}", key, e);
        }
    }

    /**
     * 获取并转换对象
     */
    public <T> T get(String key, Class<T> type) {
        return get(key, type, null);
    }

    public <T> T get(String key, Class<T> type, T defaultValue) {
        if (key == null) {
            return defaultValue;
        }
        try {
            String json = redisTemplate.opsForValue().get(key);
            if (json == null || json.trim().isEmpty()) {
                return defaultValue;
            }
            return objectMapper.readValue(json, type);
        } catch (Exception e) {
            log.error("Redis get failed for key: {}, type: {}", key, type, e);
            return defaultValue;
        }
    }

    /**
     * 获取并转换列表
     */
    public <T> List<T> getList(String key, Class<T> elementType) {
        return getList(key, elementType, Collections.emptyList());
    }

    public <T> List<T> getList(String key, Class<T> elementType, List<T> defaultValue) {
        if (key == null) return defaultValue;

        try {
            String json = redisTemplate.opsForValue().get(key);
            if (json == null || json.trim().isEmpty()) {
                return defaultValue;
            }

            JavaType listType = objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, elementType);
            return objectMapper.readValue(json, listType);

        } catch (Exception e) {
            log.error("Redis getList failed for key: {}, type: {}", key, elementType, e);
            return defaultValue;
        }
    }

    /**
     * 获取并转换复杂类型（Map、Set等）
     */
    public <T> T get(String key, TypeReference<T> typeReference) {
        return get(key, typeReference, null);
    }

    public <T> T get(String key, TypeReference<T> typeReference, T defaultValue) {
        if (key == null) {
            return defaultValue;
        }

        try {
            String json = redisTemplate.opsForValue().get(key);
            if (json == null || json.trim().isEmpty()) {
                return defaultValue;
            }
            return objectMapper.readValue(json, typeReference);
        } catch (Exception e) {
            log.error("Redis get failed for key: {}, type: {}", key, typeReference, e);
            return defaultValue;
        }
    }

    public void delete(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                //springboot2.4后用法
                redisTemplate.delete(Arrays.asList(key));
            }
        }
    }
}
