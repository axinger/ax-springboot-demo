package com.github.axinger.service;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

@Slf4j
@Service
public class DelayedRunner1 implements ApplicationRunner {

    @Resource
    private RedissonQueue redissonQueue;

    @Resource
    private ThreadPoolTaskExecutor executor;

    @Override
    public void run(ApplicationArguments args) {
        RBlockingDeque<String> blockingDeque = redissonQueue.blockingDeque();
        executor.execute(() -> {
            while (true) {
                if (redissonQueue.isShutdown()) {
                    return;
                } else {
                    try {
                        String msg = blockingDeque.take(); // 阻塞等待消息
                        log.info("收到延迟消息,msg={} ", msg);
                        // 在这里处理你的业务逻辑
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        });

    }

    // 初始化，启动服务就执行一次
    @PostConstruct
    public void init() {
        redissonQueue.delayedQueue();
    }

    @PreDestroy
    public void shutdown() {
        redissonQueue.shutdown();
    }

}
