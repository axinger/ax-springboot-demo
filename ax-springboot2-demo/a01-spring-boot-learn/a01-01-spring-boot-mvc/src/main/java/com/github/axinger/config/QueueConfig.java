package com.github.axinger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

// 在配置类中定义Bean
@Configuration
public class QueueConfig {

    // 有界阻塞队列
    @Bean
    public BlockingQueue<String> boundedBlockingQueue() {
        return new ArrayBlockingQueue<>(1000); // 容量1000
    }

    // 无界阻塞队列
    @Bean
    public BlockingQueue<String> unboundedBlockingQueue() {
        return new LinkedBlockingQueue<>();
    }

    // 优先级阻塞队列
    @Bean
    public BlockingQueue<Integer> priorityBlockingQueue() {
        return new PriorityBlockingQueue<>();
    }

    // 同步移交队列（适用于生产者-消费者模式）
    @Bean
    public BlockingQueue<Object> synchronousQueue() {
        return new SynchronousQueue<>();
    }
}
