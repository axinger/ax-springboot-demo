package com.github.axinger.service.scheduled;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.CronSchedule;
import org.redisson.api.RScheduledExecutorService;
import org.redisson.api.RedissonClient;
import org.redisson.api.WorkerOptions;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Redis 定时任务服务 — 基于 RScheduledExecutorService
 * <p>
 * Redisson 的 RScheduledExecutorService 是一个分布式调度器：
 * <ul>
 *   <li>任务注册到 Redis，集群中只有一个节点执行（分布式锁保证）</li>
 *   <li>支持延迟任务、固定频率、CRON 表达式</li>
 *   <li>任务执行记录持久化在 Redis</li>
 *   <li>节点故障时任务自动转移到其他节点</li>
 * </ul>
 * <p>
 * 对比方案:
 * <ul>
 *   <li>@Scheduled (Spring): 单机定时，多实例重复执行</li>
 *   <li>XXL-Job / Quartz: 重量级调度框架</li>
 *   <li>RScheduledExecutorService: 轻量分布式，无额外依赖</li>
 * </ul>
 */
@Slf4j
@Service
public class RedisScheduledTaskService {

    private static final String EXECUTOR_NAME = "demo-scheduled-executor";
    private static final int MAX_TASK_RECORDS = 200;

    /** 内存中的任务执行记录 */
    private final ConcurrentLinkedQueue<TaskExecutionRecord> records = new ConcurrentLinkedQueue<>();

    /** 活跃的 ScheduledFuture，用于取消 */
    private final ConcurrentHashMap<String, ScheduledFuture<?>> activeFutures = new ConcurrentHashMap<>();

    @Resource
    private RedissonClient redissonClient;

    private RScheduledExecutorService executorService;

    @PostConstruct
    public void init() {
        executorService = redissonClient.getExecutorService(EXECUTOR_NAME);
        // 注册 Worker（任务处理器），最多 4 个并发线程
        executorService.registerWorkers(WorkerOptions.defaults().workers(4));
        log.info("[定时任务] RScheduledExecutorService 已启动, executor={}", EXECUTOR_NAME);
    }

    @PreDestroy
    public void destroy() {
        // 取消所有本地 Future
        activeFutures.values().forEach(f -> f.cancel(false));
        activeFutures.clear();
        // 关闭 ExecutorService（不关闭共享 RedissonClient）
        executorService.shutdown();
        log.info("[定时任务] 服务已关闭");
    }

    // ==================== 1. 一次性延迟任务 ====================

    /**
     * 提交一次性延迟任务（类似 RDelayedQueue 但使用调度器）
     */
    public String scheduleOneShot(String taskName, long delaySeconds) {
        String taskId = taskName + "-" + System.currentTimeMillis();
        ScheduledFuture<?> future = executorService.schedule(
                (Runnable & java.io.Serializable) () -> executeTask(taskId, taskName, "ONE_SHOT"),
                delaySeconds, TimeUnit.SECONDS);
        activeFutures.put(taskId, future);
        log.info("[定时任务-ONE_SHOT] 任务已提交: taskId={}, 延迟={}s", taskId, delaySeconds);
        return taskId;
    }

    // ==================== 2. 固定频率任务 ====================

    /**
     * 提交固定频率任务（上次开始 → interval → 下次开始）
     */
    public String scheduleAtFixedRate(String taskName, long intervalSeconds) {
        return scheduleAtFixedRate(taskName, 0, intervalSeconds);
    }

    /**
     * 提交固定频率任务（带初始延迟）
     */
    public String scheduleAtFixedRate(String taskName, long initialDelaySeconds, long intervalSeconds) {
        String taskId = taskName + "-" + System.currentTimeMillis();
        ScheduledFuture<?> future = executorService.scheduleAtFixedRate(
                (Runnable & java.io.Serializable) () -> executeTask(taskId, taskName, "FIXED_RATE"),
                initialDelaySeconds, intervalSeconds, TimeUnit.SECONDS);
        activeFutures.put(taskId, future);
        log.info("[定时任务-FIXED_RATE] 任务已提交: taskId={}, 间隔={}s", taskId, intervalSeconds);
        return taskId;
    }

    // ==================== 3. CRON 定时任务 ====================

    /**
     * 提交 CRON 定时任务
     * <p>
     * CronSchedule 支持 7 位 cron 表达式：秒 分 时 日 月 周 [年]
     * <pre>
     * "0/10 * * * * ?" — 每10秒
     * "0 * * * * ?"   — 每分钟
     * "0 0 8 * * ?"   — 每天早上8点
     * </pre>
     */
    public String scheduleCron(String taskName, String cronExpression) {
        String taskId = taskName + "-" + System.currentTimeMillis();
        CronSchedule cron = CronSchedule.of(cronExpression);
        ScheduledFuture<?> future = executorService.schedule(
                (Runnable & java.io.Serializable) () -> executeTask(taskId, taskName, "CRON"),
                cron);
        activeFutures.put(taskId, future);
        log.info("[定时任务-CRON] 任务已提交: taskId={}, cron={}", taskId, cronExpression);
        return taskId;
    }

    // ==================== 任务执行 & 取消 ====================

    private void executeTask(String taskId, String taskName, String type) {
        TaskExecutionRecord record = new TaskExecutionRecord();
        record.setTaskId(taskId);
        record.setTaskName(taskName);
        record.setType(type);
        record.setThreadName(Thread.currentThread().getName());
        record.setExecuteTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        // 模拟业务逻辑
        record.setResult("执行成功 @ " + record.getExecuteTime());

        records.offer(record);
        // 控制内存中的记录数
        while (records.size() > MAX_TASK_RECORDS) {
            records.poll();
        }
        log.info("[定时任务-执行] type={}, taskId={}, name={}, time={}", type, taskId, taskName, record.getExecuteTime());
    }

    /**
     * 取消指定任务
     */
    public boolean cancelTask(String taskId) {
        ScheduledFuture<?> future = activeFutures.remove(taskId);
        if (future != null) {
            boolean cancelled = future.cancel(false);
            log.info("[定时任务-取消] taskId={}, cancelled={}", taskId, cancelled);
            return cancelled;
        }
        log.warn("[定时任务-取消] 未找到任务: taskId={}", taskId);
        return false;
    }

    /**
     * 取消所有任务
     */
    public int cancelAllTasks() {
        int count = activeFutures.size();
        activeFutures.values().forEach(f -> f.cancel(false));
        activeFutures.clear();
        records.clear();
        log.info("[定时任务-取消] 已取消所有任务, 共 {} 个", count);
        return count;
    }

    // ==================== 查询 ====================

    public List<TaskExecutionRecord> getExecutionRecords() {
        return new ArrayList<>(records);
    }

    public List<String> getActiveTaskIds() {
        return new ArrayList<>(activeFutures.keySet());
    }

    /**
     * 获取任务概览
     */
    public Map<String, Object> getOverview() {
        return Map.of(
                "executorName", EXECUTOR_NAME,
                "activeTaskCount", activeFutures.size(),
                "totalExecutionCount", records.size(),
                "recentExecutions", records.stream()
                        .skip(Math.max(0, records.size() - 10))
                        .collect(Collectors.toList())
        );
    }

    // ==================== 模型 ====================

    @Data
    public static class TaskExecutionRecord implements java.io.Serializable {
        private String taskId;
        private String taskName;
        private String type;
        private String threadName;
        private String executeTime;
        private String result;
    }
}
