package com.github.axinger.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Slf4j
@Component
@CacheConfig(cacheNames = {"student", "person"})
public class CacheService {

    @Autowired
    private CacheManager cacheManager;

    /**
     * 查询
     */
//    @Cacheable(value = "dog", key = "#id")
    @Cacheable(key = "#id")
//    @Cacheable(keyGenerator = "keyGenerator")
    public <K, V> V get(K id) {
        log.info("查询id={}", id);
        return null;
    }

    /**
     * 插入或者更新
     */

    @CachePut(key = "#id", unless = "#result == null")
//    @CachePut(keyGenerator = "keyGenerator", unless = "#result == null")
    public <K, V> V put(K id, V value) {
        log.info("插入或者更新id={},value={}", id, value);
        return value;
    }

    /**
     * 删除
     */
    @CacheEvict(key = "#id")
    public <K> boolean remove(K id) {
        log.info("删除id={}", id);
        return true;
    }

    @CacheEvict(allEntries = true)
    public void removeAll() {
        log.info("删除所有");
    }

    /**
     * 查询某个缓存的所有 key
     */
    public List<Object> getAllKeys(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            return new ArrayList<>();
        }

        // 获取原生缓存实现（如 Redis、Caffeine、Ehcache）
        Object nativeCache = Objects.requireNonNull(cache.getNativeCache());

        List<Object> keys = new ArrayList<>();
        if (nativeCache instanceof com.github.benmanes.caffeine.cache.Cache) {
            // Caffeine 缓存
            keys.addAll(((com.github.benmanes.caffeine.cache.Cache<?, ?>) nativeCache).asMap().keySet());
//        } else if (nativeCache instanceof org.springframework.data.redis.cache.RedisCache) {
//            // Redis 缓存（需要额外处理）
//            keys.addAll(getRedisCacheKeys(cacheName));
//        } else if (nativeCache instanceof net.sf.ehcache.Ehcache) {
//            // Ehcache
//            keys.addAll(((net.sf.ehcache.Ehcache) nativeCache).getKeys());
        } else {
            throw new UnsupportedOperationException("Unsupported cache type: " + nativeCache.getClass());
        }

        return keys;
    }
//
//    /**
//     * 查询 Redis 缓存的所有 key（需要 RedisTemplate）
//     */
//    private List<Object> getRedisCacheKeys(String cacheName) {
//        // 假设你有 RedisTemplate<String, Object> redisTemplate
//        // Redis 缓存的 key 通常是 "cacheName::key" 格式
//        String pattern = cacheName + "::*";
//        Set<String> keys = redisTemplate.keys(pattern);
//        return new ArrayList<>(keys);
//    }

    /**
     * 查询所有缓存名称
     */
    public List<String> getAllCacheNames() {
        return new ArrayList<>(cacheManager.getCacheNames());
    }

    public Object getCacheKey(String cacheName, String key) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            return null;
        }
        Cache.ValueWrapper valueWrapper = cache.get(key);
        return Optional.ofNullable(valueWrapper).map(Cache.ValueWrapper::get).orElse(null);
    }
}
