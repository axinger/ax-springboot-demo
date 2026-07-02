package com.github.axinger.controller;

import com.github.axinger.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 消息 Controller — 普通消息 + 延迟消息 + Stream
 *
 * <pre>
 *   GET /message/topic/send      — RTopic 普通 Pub/Sub
 *   GET /message/sharded/send    — RShardedTopic 分片 (Redis 7.0+)
 *   GET /message/delayed/send    — RDelayedQueue 延迟消息
 *   GET /message/stream/send     — RStream 持久化 + 消费者组
 *   GET /message/result          — 查看全部消息
 * </pre>
 */
@Tag(name = "消息Demo", description = "普通消息(Topic) + 延迟消息(DelayedQueue) + 持久化消息(Stream)")
@RestController
@RequestMapping("/message")
public class MessageController {

    @Resource
    private MessageService svc;

    // ==================== 普通消息 ====================

    @Operation(summary = "发送普通消息", description = "RTopic Pub/Sub — 发布后所有订阅者立即收到")
    @GetMapping("/topic/send")
    public Map<String, Object> sendTopic(@RequestParam(defaultValue = "Hello") String content) {
        svc.sendTopic(content);
        return Map.of("status", "ok", "type", "RTopic Pub/Sub");
    }

    @Operation(summary = "发送分片消息", description = "RShardedTopic — Redis 7.0+, 避免集群全量广播")
    @GetMapping("/sharded/send")
    public Map<String, Object> sendSharded(@RequestParam(defaultValue = "Hello") String content) {
        svc.sendSharded(content);
        return Map.of("status", "ok", "type", "RShardedTopic");
    }

    // ==================== 延迟消息 ====================

    @Operation(summary = "发送延迟消息", description = "RDelayedQueue(ZSet实现) — 消息 N 秒后自动投递消费")
    @GetMapping("/delayed/send")
    public Map<String, Object> sendDelayed(@RequestParam(defaultValue = "延迟消息") String content,
                                           @RequestParam(defaultValue = "5") long delay) {
        svc.sendDelayed(content, delay);
        return Map.of("status", "ok", "type", "RDelayedQueue",
                "queue", MessageService.DELAY_QUEUE, "delaySeconds", delay);
    }

    // ==================== Stream 持久化消息 ====================

    @Operation(summary = "发送Stream消息", description = "RStream — 持久化+消费者组+ACK确认, 类似Kafka")
    @GetMapping("/stream/send")
    public Map<String, Object> sendStream(@RequestParam(defaultValue = "Hello Stream") String content) {
        return Map.of("status", "ok", "type", "RStream", "msgId", svc.sendStream(content));
    }

    // ==================== 查询 & 清空 ====================

    @GetMapping("/result")
    public Map<String, Object> result() {
        return svc.allMessages();
    }

    @DeleteMapping("/clear")
    public Map<String, Object> clear() {
        svc.clearAll();
        return Map.of("status", "ok");
    }

    @Operation(summary = "消息模式概览")
    @GetMapping("/overview")
    public Map<String, Object> overview() {
        return Map.of(
                "title", "Redisson 消息系统",
                "RTopic", "Redis 2.0+ | Pub/Sub | 即发即忘",
                "RShardedTopic", "Redis 7.0+ | 分片Pub/Sub | 避免广播风暴",
                "RPatternTopic", "Redis 2.0+ | 模式匹配 | 通配符 * ?",
                "RDelayedQueue", "Redis 2.0+ | ZSet延迟 | 消息定时投递",
                "RStream", "Redis 5.0+ | 持久化+消费者组 | 类似Kafka"
        );
    }
}
