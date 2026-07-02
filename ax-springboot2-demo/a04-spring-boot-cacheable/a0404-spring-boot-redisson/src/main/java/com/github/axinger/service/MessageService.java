package com.github.axinger.service;

import com.github.axinger.model.MessageBody;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.redisson.api.stream.StreamAddArgs;
import org.redisson.api.stream.StreamReadGroupArgs;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 消息服务 — Redisson 原生 API 编程范式
 * <pre>
 *  RTopic        — Pub/Sub 普通消息
 *  RShardedTopic — 分片订阅 (Redis 7.0+)
 *  RDelayedQueue — 延迟队列, ZSet 实现
 *  RStream       — 持久化流 + 消费者组 (Redis 5.0+)
 * </pre>
 */
@Slf4j
@Service
public class MessageService {

    @Resource
    private RedissonClient redissonClient;

    public static final String TOPIC_CHANNEL = "demo:topic:channel";
    public static final String SHARDED_CHANNEL = "demo:sharded:channel";
    public static final String DELAY_QUEUE = "demo:delayed:queue";
    public static final String STREAM_KEY = "demo:stream:messages";
    private static final int MAX_CACHED = 100;

    // 缓存队列实例
    private RBlockingQueue<MessageBody> blockingQueue;
    private RDelayedQueue<MessageBody> delayedQueue;
    private RStream<String, MessageBody> stream;

    // 内存消息记录
    private final List<MessageBody> topicMsgs = new ArrayList<>();
    private final List<MessageBody> shardedMsgs = new ArrayList<>();
    private final List<MessageBody> delayedMsgs = new ArrayList<>();
    private final List<MessageBody> streamMsgs = new ArrayList<>();

    private final List<Thread> threads = new ArrayList<>();
    private final AtomicBoolean running = new AtomicBoolean(true);

    @PostConstruct
    public void init() {
        // 缓存队列
        blockingQueue = redissonClient.getBlockingQueue(DELAY_QUEUE);
        delayedQueue = redissonClient.getDelayedQueue(blockingQueue);
        stream = redissonClient.getStream(STREAM_KEY);

        // === 订阅监听 (纯 Redisson API, 无注解包装) ===
        redissonClient.getTopic(TOPIC_CHANNEL)
                .addListener(MessageBody.class, (ch, msg) -> cache(topicMsgs, msg));
        log.info("[消息] RTopic 已订阅: {}", TOPIC_CHANNEL);

        redissonClient.getShardedTopic(SHARDED_CHANNEL)
                .addListener(MessageBody.class, (ch, msg) -> cache(shardedMsgs, msg));
        log.info("[消息] ShardedTopic 已订阅: {}", SHARDED_CHANNEL);

        redissonClient.getPatternTopic("demo:*")
                .addListener(MessageBody.class, (p, c, m) -> log.info("[Pattern] {} → {}", c, m.getContent()));

        // === 启动消费者 ===
        startDelayedConsumer();
        startStreamConsumer();
    }

    // ==================== 普通消息 ====================

    public long sendTopic(String content) {
        return redissonClient.getTopic(TOPIC_CHANNEL).publish(MessageBody.of(content));
    }

    public long sendSharded(String content) {
        return redissonClient.getShardedTopic(SHARDED_CHANNEL).publish(MessageBody.of(content));
    }

    // ==================== 延迟消息 ====================

    public void sendDelayed(String content, long delaySeconds) {
        delayedQueue.offer(MessageBody.ofDelayed(content, delaySeconds), delaySeconds, TimeUnit.SECONDS);
        log.info("[延迟] 入队: {} ({}s)", content, delaySeconds);
    }

    private void startDelayedConsumer() {
        Thread t = new Thread(() -> {
            while (running.get()) {
                try {
                    cache(delayedMsgs, blockingQueue.take());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "delayed-consumer");
        t.setDaemon(true);
        t.start();
        threads.add(t);
    }

    // ==================== Stream 消息 ====================

    public String sendStream(String content) {
        MessageBody msg = MessageBody.of(content);
        return stream.add(StreamAddArgs.entry(msg.getId(), msg)).toString();
    }

    private void startStreamConsumer() {
        try { stream.createGroup("demo-group"); } catch (Exception ignored) {}

        Thread t = new Thread(() -> {
            while (running.get()) {
                try {
                    Map<StreamMessageId, Map<String, MessageBody>> msgs =
                            stream.readGroup("demo-group", "consumer-1",
                                    StreamReadGroupArgs.greaterThan(StreamMessageId.NEVER_DELIVERED));
                    if (msgs != null) msgs.forEach((id, data) -> {
                        data.values().forEach(m -> cache(streamMsgs, m));
                        stream.ack("demo-group", id);
                    });
                } catch (Exception ignored) {
                    // readGroup 超时或无消息时抛异常, 继续轮询
                }
            }
        }, "stream-consumer");
        t.setDaemon(true);
        t.start();
        threads.add(t);
    }

    // ==================== 查询 ====================

    public List<MessageBody> getTopicMessages()   { return new ArrayList<>(topicMsgs); }
    public List<MessageBody> getShardedMessages() { return new ArrayList<>(shardedMsgs); }
    public List<MessageBody> getDelayedMessages() { return new ArrayList<>(delayedMsgs); }
    public List<MessageBody> getStreamMessages()  { return new ArrayList<>(streamMsgs); }

    public Map<String, Object> allMessages() {
        return Map.of("topic", getTopicMessages(), "delayed", getDelayedMessages(), "stream", getStreamMessages());
    }

    public void clearAll() {
        topicMsgs.clear();
        shardedMsgs.clear();
        delayedMsgs.clear();
        streamMsgs.clear();
        delayedQueue.clear();
    }

    private void cache(List<MessageBody> list, MessageBody msg) {
        if (list.size() >= MAX_CACHED) list.clear();
        list.add(msg);
    }

    @PreDestroy
    public void destroy() {
        running.set(false);
        threads.forEach(Thread::interrupt);
    }
}
