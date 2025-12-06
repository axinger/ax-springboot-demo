package com.github.axinger.service;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class DelayedMessageService {

    @Autowired
    private RedissonClient redissonClient;

    private RBlockingQueue<String> blockingQueue;
    private RDelayedQueue<String> delayedQueue;

    @PostConstruct
    public void init() {
        // 获取阻塞队列（消费者监听）
        blockingQueue = redissonClient.getBlockingQueue("delayed-queue");
        // 绑定延迟队列到该阻塞队列
        delayedQueue = redissonClient.getDelayedQueue(blockingQueue);

        startConsumer();
    }

    @PreDestroy
    public void destroy() {
        // 停止消费者线程
        redissonClient.shutdown();
    }

    /**
     * 发送延迟消息
     */
    public void sendMessage(String message, long delay, TimeUnit unit) {
        delayedQueue.offer(message, delay, unit);
        System.out.println("消息已加入延迟队列: " + message + ", 延迟: " + delay + " " + unit);
    }

    /**
     * 启动消费者线程（实际项目中建议用单独线程池或异步任务）
     */
    public void startConsumer() {
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    String msg = blockingQueue.take(); // 阻塞等待消息
                    log.info("收到延迟消息msg={} ", msg);
                    // 在这里处理你的业务逻辑
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }
}
