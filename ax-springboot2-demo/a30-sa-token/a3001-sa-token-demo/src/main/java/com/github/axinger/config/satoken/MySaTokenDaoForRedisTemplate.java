package com.github.axinger.config.satoken;

import cn.dev33.satoken.dao.SaTokenDaoForRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Sa-Token 持久层实现 [ Redis 存储 ] (可用环境: SpringBoot2、SpringBoot3)
 *
 * @author click33
 * @since 1.34.0
 */
@Component
public class MySaTokenDaoForRedisTemplate extends SaTokenDaoForRedisTemplate {

    private static final String REDIS_CACHE_PREFIX = "axinger:demo3001:";

    /**
     * 增加sa-token相关redis cache前缀
     */
    private static String appendCachePrefix(String key) {
        if (key == null) {
            return null;
        }
        if (key.startsWith(REDIS_CACHE_PREFIX)) {
            return key;
        }
        return REDIS_CACHE_PREFIX + key;
    }


    /**
     * 获取Value，如无返空
     */
    @Override
    public String get(String key) {
        key = appendCachePrefix(key);
        return super.get(key);
    }

    /**
     * 写入Value，并设定存活时间 (单位: 秒)
     */
    @Override
    public void set(String key, String value, long timeout) {
        key = appendCachePrefix(key);
        super.set(key, value, timeout);
    }

    /**
     * 修改指定key-value键值对 (过期时间不变)
     */
    @Override
    public void update(String key, String value) {
        key = appendCachePrefix(key);
        super.update(key, value);
    }

    /**
     * 删除Value
     */
    @Override
    public void delete(String key) {
        key = appendCachePrefix(key);
        super.delete(key);
    }

    /**
     * 获取Value的剩余存活时间 (单位: 秒)
     */
    @Override
    public long getTimeout(String key) {
        key = appendCachePrefix(key);
        return super.getTimeout(key);
    }

    /**
     * 修改Value的剩余存活时间 (单位: 秒)
     */
    @Override
    public void updateTimeout(String key, long timeout) {
        key = appendCachePrefix(key);
        super.updateTimeout(key, timeout);
    }


}
