package com.axing.common.executor.bea;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "axinger.thread-pool")
public class ExecutorProperties {

    /**
     * 核心线程数
     */
//    private int corePoolSize = 8;
    private int corePoolSize = Runtime.getRuntime().availableProcessors();

    /**
     * 最大线程数
     */
    private int maxPoolSize = Runtime.getRuntime().availableProcessors() + 1;

    /**
     * 队列保活时长
     */
    private int keepAliveSeconds = 60;

    /**
     * 队列最大数
     */
    private int queueCapacity = 400;

    /**
     * 线程池前缀
     */
    private String threadNamePrefix = "👉️默认线程池名称-👈";
}
