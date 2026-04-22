package com.axing.common.redis.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "axinger.redis")
public class RedisProperties {
    /**
     * 自定义对象,存入是否需要全路径包名,方便反序列化
     */
    private boolean savePackageName = false;
}
