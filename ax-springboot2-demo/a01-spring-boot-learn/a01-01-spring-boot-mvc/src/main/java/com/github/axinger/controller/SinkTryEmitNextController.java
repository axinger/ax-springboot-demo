package com.github.axinger.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 基于 Sinks.tryEmitNext 的顺序队列处理器
 * <p>
 * 核心特性：
 * 1. 使用 for 循环 + sink.tryEmitNext 批量提交任务
 * 2. 严格保证事件处理顺序（FIFO）
 * 3. 支持背压控制，防止内存溢出
 * 4. 异步非阻塞处理
 *
 * @author demo
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/api/sink-queue")
public class SinkTryEmitNextController {

    // ==================== 内部类定义 ====================

    /**
     * 用户事件类型枚举
     */
    public static enum EventType {
        LOGIN("登录"),
        KICKOUT("踢下线"),
        LOGOUT("登出");

        private final String description;

        EventType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 用户事件实体类
     */
    public static class UserEvent {
        private String userId;
        private EventType type;
        private LocalDateTime timestamp;
        private String detail;

        public UserEvent() {
        }

        public UserEvent(String userId, EventType type, String detail) {
            this.userId = userId;
            this.type = type;
            this.timestamp = LocalDateTime.now();
            this.detail = detail;
        }

        // Getters and Setters
        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public EventType getType() {
            return type;
        }

        public void setType(EventType type) {
            this.type = type;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        @Override
        public String toString() {
            return String.format("UserEvent{userId='%s', type=%s, time=%s}", userId, type, timestamp);
        }
    }

    /**
     * 队列状态 DTO
     */
    public static class QueueStatus {
        private long submittedCount;
        private long processedCount;
        private long pendingCount;
        private boolean running;

        public QueueStatus(long submittedCount, long processedCount, boolean running) {
            this.submittedCount = submittedCount;
            this.processedCount = processedCount;
            this.pendingCount = submittedCount - processedCount;
            this.running = running;
        }

        public long getSubmittedCount() {
            return submittedCount;
        }

        public long getProcessedCount() {
            return processedCount;
        }

        public long getPendingCount() {
            return pendingCount;
        }

        public boolean isRunning() {
            return running;
        }
    }

    // ==================== 核心组件 ====================

    /**
     * Sinks 队列 - 支持背压的多播队列
     */
    private final Sinks.Many<String> sink = Sinks.many()
            .multicast()
            .onBackpressureBuffer(1000);

    /**
     * 字符串队列（用于简单任务）
     */
    private final Sinks.Many<String> stringSink = Sinks.many()
            .multicast()
            .onBackpressureBuffer(1000);

    /**
     * 对象队列（用于复杂任务）
     */
    private final Sinks.Many<UserEvent> eventSink = Sinks.many()
            .multicast()
            .onBackpressureBuffer(1000);

    /**
     * 已提交计数器
     */
    private final AtomicLong submittedCount = new AtomicLong(0);

    /**
     * 已处理计数器
     */
    private final AtomicLong processedCount = new AtomicLong(0);

    /**
     * 运行状态
     */
    private volatile boolean running = true;

    /**
     * 构造函数 - 初始化所有消费者
     */
    public SinkTryEmitNextController() {
        // 启动字符串消费者
        startStringConsumer();

        // 启动事件消费者
        startEventConsumer();
    }

    // ==================== 消费者初始化 ====================

    /**
     * 启动字符串队列消费者
     * 处理简单的字符串任务
     */
    private void startStringConsumer() {
        log.info("🚀 字符串队列消费者启动中...");

        stringSink.asFlux()
                .subscribeOn(Schedulers.newSingle("string-consumer"))
                .doOnNext(task -> processedCount.incrementAndGet())
                .doOnError(error -> log.error("消费者错误", error))
                .doOnSubscribe(s -> log.info("✅ 字符串消费者已订阅"))
                .subscribe(
                        task -> {
                            log.info("✅ [处理字符串] {}", task);
                            simulateWork(50);
                        },
                        error -> log.error("错误", error),
                        () -> log.info("流结束")
                );

        log.info("🎉 字符串队列消费者启动完成");
    }

    /**
     * 启动事件队列消费者
     * 处理复杂的用户事件
     */
    private void startEventConsumer() {
        log.info("🚀 事件队列消费者启动中...");

        eventSink.asFlux()
                .subscribeOn(Schedulers.newSingle("event-consumer"))
                .doOnNext(event -> log.info("📦 [处理事件] {}", event))
                .doOnError(error -> log.error("消费者错误", error))
                .doOnSubscribe(s -> log.info("✅ 事件消费者已订阅"))
                .subscribe(
                        event -> {
                            processEvent(event);
                        },
                        error -> log.error("错误", error),
                        () -> log.info("流结束")
                );

        log.info("🎉 事件队列消费者启动完成");
    }

    /**
     * 处理用户事件
     */
    private void processEvent(UserEvent event) {
        long startTime = System.currentTimeMillis();

        try {
            switch (event.getType()) {
                case LOGIN:
                    log.info("🔐 登录业务 - 用户: {}, 详情: {}", event.getUserId(), event.getDetail());
                    break;
                case KICKOUT:
                    log.info("👢 踢下线 - 用户: {}, 详情: {}", event.getUserId(), event.getDetail());
                    break;
                case LOGOUT:
                    log.info("🚪 登出 - 用户: {}", event.getUserId());
                    break;
            }

            simulateWork(50);

            long costTime = System.currentTimeMillis() - startTime;
            log.info("✅ 事件处理完成, 耗时: {}ms", costTime);

        } catch (Exception e) {
            log.error("❌ 事件处理失败", e);
        }
    }

    /**
     * 模拟工作负载
     */
    private void simulateWork(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // ==================== 方案1: 简单字符串任务 ====================

    /**
     * 方案1: 基础版 - 10个字符串任务
     * 使用 for 循环 + sink.tryEmitNext
     */
    @GetMapping("/demo1/basic")
    public Mono<Map<String, Object>> demo1Basic() {
        log.info("========== 方案1: 基础版 - 提交10个任务 ==========");

        // ✅ 核心代码：for 循环 + tryEmitNext
        for (int i = 1; i <= 10; i++) {
            String task = "任务-" + i;
            Sinks.EmitResult result = stringSink.tryEmitNext(task);

            if (result.isSuccess()) {
                submittedCount.incrementAndGet();
                log.info("📥 提交成功: {}", task);
            } else {
                log.warn("❌ 提交失败: {}, 原因: {}", task, result);
            }
        }

        return Mono.just(Map.of(
                "code", 200,
                "message", "已提交10个任务",
                "submitted", submittedCount.get(),
                "方案", "基础版 - for循环 + tryEmitNext"
        ));
    }

    // ==================== 方案2: 批量提交带数量参数 ====================

    /**
     * 方案2: 批量提交 - 支持指定数量
     *
     * @param count 任务数量，默认10，最大1000
     */
    @GetMapping("/demo2/batch")
    public Mono<Map<String, Object>> demo2Batch(@RequestParam(defaultValue = "10") int count) {
        // 限制最大数量
        int actualCount = Math.min(count, 1000);
        log.info("========== 方案2: 批量提交 - 数量: {} ==========", actualCount);

        int successCount = 0;
        int failCount = 0;

        // ✅ 核心代码：for 循环批量提交
        for (int i = 1; i <= actualCount; i++) {
            String task = String.format("批量任务-%04d", i);
            Sinks.EmitResult result = stringSink.tryEmitNext(task);

            if (result.isSuccess()) {
                successCount++;
                submittedCount.incrementAndGet();

                // 每100个打印一次进度
                if (i % 100 == 0) {
                    log.info("📊 提交进度: {}/{}", i, actualCount);
                }
            } else {
                failCount++;
                log.warn("❌ 提交失败: {}, 原因: {}", task, result);
            }
        }

        log.info("✅ 批量提交完成 - 成功: {}, 失败: {}", successCount, failCount);

        return Mono.just(Map.of(
                "code", 200,
                "message", "批量提交完成",
                "total", actualCount,
                "success", successCount,
                "failed", failCount,
                "方案", "批量提交 - 支持数量参数"
        ));
    }

    // ==================== 方案3: 登录场景演示 ====================

    /**
     * 方案3: 模拟用户登录场景
     * 批量提交10个登录事件
     */
    @GetMapping("/demo3/login-batch")
    public Mono<Map<String, Object>> demo3LoginBatch() {
        log.info("========== 方案3: 批量登录场景 ==========");

        int successCount = 0;

        // ✅ 核心代码：for 循环提交登录事件
        for (int i = 1; i <= 10; i++) {
            String userId = "user_" + String.format("%03d", i);
            String task = String.format("【登录】用户:%s, 时间:%s", userId, LocalDateTime.now());

            Sinks.EmitResult result = stringSink.tryEmitNext(task);

            if (result.isSuccess()) {
                successCount++;
                submittedCount.incrementAndGet();
                log.info("📥 登录请求已提交: {}", userId);
            }
        }

        return Mono.just(Map.of(
                "code", 200,
                "message", String.format("已提交 %d 个登录请求", successCount),
                "方案", "批量登录场景"
        ));
    }

    // ==================== 方案4: 复杂事件批量提交 ====================

    /**
     * 方案4: 复杂事件 - 使用对象队列
     * 批量提交用户事件（登录、踢下线、登出）
     */
    @GetMapping("/demo4/event-batch")
    public Mono<Map<String, Object>> demo4EventBatch() {
        log.info("========== 方案4: 复杂事件批量提交 ==========");

        int successCount = 0;

        // ✅ 核心代码：for 循环提交复杂对象
        for (int i = 1; i <= 10; i++) {
            String userId = "event_user_" + i;
            EventType type;
            String detail;

            // 轮流使用不同类型
            if (i % 3 == 1) {
                type = EventType.LOGIN;
                detail = "用户登录系统";
            } else if (i % 3 == 2) {
                type = EventType.KICKOUT;
                detail = "管理员强制踢出";
            } else {
                type = EventType.LOGOUT;
                detail = "用户主动登出";
            }

            UserEvent event = new UserEvent(userId, type, detail);
            Sinks.EmitResult result = eventSink.tryEmitNext(event);

            if (result.isSuccess()) {
                successCount++;
                log.info("📥 事件已提交: {} - {}", userId, type.getDescription());
            } else {
                log.warn("❌ 事件提交失败: {}", userId);
            }
        }

        return Mono.just(Map.of(
                "code", 200,
                "message", "复杂事件批量提交完成",
                "successCount", successCount,
                "方案", "复杂事件 - 对象队列"
        ));
    }

    // ==================== 方案5: 严格顺序演示 ====================

    /**
     * 方案5: 严格顺序演示
     * 同一个用户：登录 -> 踢下线 -> 再次登录
     * 保证严格按时间顺序执行
     */
    @GetMapping("/demo5/sequential")
    public Mono<Map<String, Object>> demo5Sequential() {
        String userId = "sequential_user_001";
        log.info("========== 方案5: 严格顺序演示 - 用户: {} ==========", userId);

        // ✅ 核心代码：按顺序提交3个任务
        // 任务1: 登录
        String task1 = String.format("【1-登录】用户:%s, Session:session-1, 时间:%s",
                userId, LocalDateTime.now());
        stringSink.tryEmitNext(task1);
        log.info("📥 提交: {}", task1);

        // 任务2: 踢下线
        String task2 = String.format("【2-踢下线】用户:%s, 原因:管理员强制踢出, 时间:%s",
                userId, LocalDateTime.now());
        stringSink.tryEmitNext(task2);
        log.info("📥 提交: {}", task2);

        // 任务3: 再次登录
        String task3 = String.format("【3-登录】用户:%s, Session:session-2, 时间:%s",
                userId, LocalDateTime.now());
        stringSink.tryEmitNext(task3);
        log.info("📥 提交: {}", task3);

        return Mono.just(Map.of(
                "code", 200,
                "message", "顺序任务已提交，将按 登录 -> 踢下线 -> 登录 顺序执行",
                "userId", userId,
                "tasks", new String[]{task1, task2, task3},
                "方案", "严格顺序演示"
        ));
    }

    // ==================== 方案6: 压力测试 ====================

    /**
     * 方案6: 压力测试
     * 一次性提交大量任务，测试队列性能
     *
     * @param count 任务数量，默认100
     */
    @GetMapping("/demo6/stress")
    public Mono<Map<String, Object>> demo6Stress(@RequestParam(defaultValue = "100") int count) {
        int actualCount = Math.min(count, 10000);
        long startTime = System.currentTimeMillis();

        log.info("========== 方案6: 压力测试 - 提交 {} 个任务 ==========", actualCount);

        int successCount = 0;
        int failCount = 0;

        // ✅ 核心代码：for 循环压力测试
        for (int i = 1; i <= actualCount; i++) {
            String task = String.format("压测任务-%08d", i);
            Sinks.EmitResult result = stringSink.tryEmitNext(task);

            if (result.isSuccess()) {
                successCount++;
                submittedCount.incrementAndGet();
            } else {
                failCount++;
            }

            // 每1000个打印一次进度
            if (i % 1000 == 0) {
                log.info("📊 压力测试进度: {}/{}", i, actualCount);
            }
        }

        long costTime = System.currentTimeMillis() - startTime;
        log.info("✅ 压力测试完成 - 成功: {}, 失败: {}, 耗时: {}ms",
                successCount, failCount, costTime);

        return Mono.just(Map.of(
                "code", 200,
                "message", "压力测试完成",
                "total", actualCount,
                "success", successCount,
                "failed", failCount,
                "costMs", costTime,
                "tps", String.format("%.2f", successCount * 1000.0 / costTime),
                "方案", "压力测试"
        ));
    }

    // ==================== 方案7: 多场景混合 ====================

    /**
     * 方案7: 多场景混合
     * 模拟真实业务场景：多个用户的不同操作
     */
    @GetMapping("/demo7/mixed")
    public Mono<Map<String, Object>> demo7Mixed() {
        log.info("========== 方案7: 多场景混合 ==========");

        String[] users = {"张三", "李四", "王五", "赵六", "小明"};
        String[] operations = {"登录", "查询", "更新", "踢下线", "登出"};

        int successCount = 0;

        // ✅ 核心代码：双层 for 循环混合场景
        for (int i = 1; i <= 10; i++) {
            for (String user : users) {
                String operation = operations[(i + user.hashCode()) % operations.length];
                String task = String.format("【%s】用户:%s, 操作:%s, 序号:%d",
                        LocalDateTime.now().toString().substring(11, 19),
                        user, operation, i);

                Sinks.EmitResult result = stringSink.tryEmitNext(task);

                if (result.isSuccess()) {
                    successCount++;
                    submittedCount.incrementAndGet();
                }
            }
        }

        return Mono.just(Map.of(
                "code", 200,
                "message", "混合场景提交完成",
                "successCount", successCount,
                "users", users.length,
                "operations", operations.length,
                "方案", "多场景混合"
        ));
    }

    // ==================== 方案8: 带重试机制的批量提交 ====================

    /**
     * 方案8: 带重试机制的批量提交
     * 处理 FAIL_NON_SERIALIZED 错误
     *
     * @param count 任务数量
     */
    @GetMapping("/demo8/retry")
    public Mono<Map<String, Object>> demo8WithRetry(@RequestParam(defaultValue = "10") int count) {
        log.info("========== 方案8: 带重试机制 - 数量: {} ==========", count);

        int successCount = 0;
        int retryCount = 0;

        for (int i = 1; i <= count; i++) {
            String task = "重试任务-" + i;
            Sinks.EmitResult result = stringSink.tryEmitNext(task);

            // 处理非串行化错误 - 重试一次
            if (result == Sinks.EmitResult.FAIL_NON_SERIALIZED) {
                log.warn("检测到非串行化，重试: {}", task);
//                result = stringSink.emitNext(task, (signalType, emitResult) -> {
//                    log.debug("重试中...");
//                    return true;
//                });
                stringSink.emitNext(task, (signalType, emitResult) -> {
                    log.debug("重试中...");
                    return true;
                });
                retryCount++;
            }

            if (result.isSuccess()) {
                successCount++;
                submittedCount.incrementAndGet();
                log.info("📥 提交成功: {}", task);
            } else {
                log.error("❌ 提交失败: {}, 原因: {}", task, result);
            }
        }

        return Mono.just(Map.of(
                "code", 200,
                "message", "带重试机制的批量提交完成",
                "total", count,
                "success", successCount,
                "retryCount", retryCount,
                "方案", "带重试机制"
        ));
    }

    // ==================== 辅助接口 ====================

    /**
     * 查询队列状态
     */
    @GetMapping("/status")
    public Mono<QueueStatus> getStatus() {
        return Mono.just(new QueueStatus(
                submittedCount.get(),
                processedCount.get(),
                running
        ));
    }

    /**
     * 清空所有计数器（测试用）
     */
    @DeleteMapping("/reset")
    public Mono<Map<String, Object>> reset() {
        long oldSubmitted = submittedCount.get();
        long oldProcessed = processedCount.get();

        submittedCount.set(0);
        processedCount.set(0);

        log.info("🔄 重置计数器 - 原提交: {}, 原处理: {}", oldSubmitted, oldProcessed);

        return Mono.just(Map.of(
                "code", 200,
                "message", "计数器已重置",
                "oldSubmitted", oldSubmitted,
                "oldProcessed", oldProcessed
        ));
    }

    /**
     * 实时监控（SSE）
     */
    @GetMapping(value = "/monitor", produces = "text/event-stream")
    public Flux<String> monitor() {
        return Flux.interval(java.time.Duration.ofSeconds(1))
                .map(seq -> String.format(
                        "data: {\"timestamp\":\"%s\",\"submitted\":%d,\"processed\":%d,\"pending\":%d,\"running\":%b}\n\n",
                        LocalDateTime.now(),
                        submittedCount.get(),
                        processedCount.get(),
                        submittedCount.get() - processedCount.get(),
                        running
                ));
    }

    // ==================== 生命周期 ====================

    /**
     * 优雅关闭
     */
    @PreDestroy
    public void destroy() {
        log.info("🛑 开始优雅关闭...");
        running = false;

        long remaining = submittedCount.get() - processedCount.get();
        if (remaining > 0) {
            log.info("等待处理剩余任务: {}", remaining);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        log.info("✅ 优雅关闭完成 - 总提交: {}, 总处理: {}",
                submittedCount.get(), processedCount.get());
    }
}