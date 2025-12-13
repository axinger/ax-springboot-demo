package com.github.axinger.service;

import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class RedissonQueue {

    public static final String QUEUE = "delayQueue";

    // 默认超时时间，30秒
    public static final Integer DEFAULT_TIMEOUT = 30;

    @Resource
    private RedissonClient redissonClient;

    // 加入任务并设置到期时间
    public void offer(String taskId, Integer timeout) {
        RDelayedQueue<String> delayedQueue = delayedQueue();
        delayedQueue.offer(taskId, Objects.isNull(timeout) ? DEFAULT_TIMEOUT : timeout, TimeUnit.SECONDS);
    }

    // 移除任务
    public void remove(String taskId) {
        RDelayedQueue<String> delayedQueue = delayedQueue();
        delayedQueue.removeIf(messageId -> messageId.equals(taskId));
    }

    // 任务列表
    public RDelayedQueue<String> delayedQueue() {
        RBlockingDeque<String> blockingDeque = blockingDeque();
        return redissonClient.getDelayedQueue(blockingDeque);
    }

    public RBlockingDeque<String> blockingDeque() {
        return redissonClient.getBlockingDeque(QUEUE);
    }

    public boolean isShutdown() {
        return redissonClient.isShutdown();
    }

    public void shutdown() {
        redissonClient.shutdown();
    }

}
