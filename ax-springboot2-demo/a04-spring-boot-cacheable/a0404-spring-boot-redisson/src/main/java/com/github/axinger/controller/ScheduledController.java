package com.github.axinger.controller;

import com.github.axinger.service.scheduled.RedisScheduledTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 定时任务 Controller — RScheduledExecutorService 分布式调度
 *
 * <pre>
 * 与 @Scheduled 对比:
 *   ✅ 分布式 — 集群中仅一个节点执行
 *   ✅ 动态 — 运行时新增/取消任务
 *   ✅ 故障转移 — 节点宕机自动迁移
 *   ✅ 持久化 — 任务保存在 Redis
 *
 * 三种模式:
 *   ONE_SHOT   — 一次性延迟执行
 *   FIXED_RATE — 固定频率执行
 *   CRON       — Cron 表达式
 * </pre>
 */
@Tag(name = "定时任务Demo", description = "RScheduledExecutorService 分布式定时任务")
@RestController
@RequestMapping("/scheduled")
public class ScheduledController {

    @Resource
    private RedisScheduledTaskService scheduledTaskService;

    @Operation(summary = "一次性延迟任务", description = "延迟 N 秒执行一次")
    @GetMapping("/one-shot")
    public Map<String, Object> oneShot(@RequestParam(defaultValue = "demoTask") String name,
                                        @RequestParam(defaultValue = "5") long delaySeconds) {
        String taskId = scheduledTaskService.scheduleOneShot(name, delaySeconds);
        return Map.of("status", "ok", "type", "ONE_SHOT", "taskId", taskId,
                "desc", delaySeconds + "秒后执行一次");
    }

    @Operation(summary = "固定频率任务")
    @GetMapping("/fixed-rate")
    public Map<String, Object> fixedRate(@RequestParam(defaultValue = "fixedTask") String name,
                                          @RequestParam(defaultValue = "3") long intervalSeconds) {
        String taskId = scheduledTaskService.scheduleAtFixedRate(name, intervalSeconds);
        return Map.of("status", "ok", "type", "FIXED_RATE", "taskId", taskId,
                "intervalSeconds", intervalSeconds);
    }

    @Operation(summary = "固定频率任务(带初始延迟)")
    @GetMapping("/fixed-rate/delayed")
    public Map<String, Object> fixedRateDelayed(@RequestParam(defaultValue = "delayedFixedTask") String name,
                                                 @RequestParam(defaultValue = "5") long initialDelay,
                                                 @RequestParam(defaultValue = "2") long interval) {
        String taskId = scheduledTaskService.scheduleAtFixedRate(name, initialDelay, interval);
        return Map.of("status", "ok", "type", "FIXED_RATE(延迟启动)", "taskId", taskId,
                "initialDelay", initialDelay + "s", "interval", interval + "s");
    }

    @Operation(summary = "CRON 定时任务", description = "支持 7 位 Cron: 秒 分 时 日 月 周 [年]")
    @GetMapping("/cron")
    public Map<String, Object> cron(@RequestParam(defaultValue = "cronTask") String name,
                                     @RequestParam(defaultValue = "0/10 * * * * ?") String cronExpression) {
        String taskId = scheduledTaskService.scheduleCron(name, cronExpression);
        return Map.of("status", "ok", "type", "CRON", "taskId", taskId,
                "cronExpression", cronExpression);
    }

    @Operation(summary = "Cron 表达式参考")
    @GetMapping("/cron/examples")
    public List<Map<String, String>> cronExamples() {
        return List.of(
                Map.of("说明", "每5秒", "表达式", "0/5 * * * * ?"),
                Map.of("说明", "每10秒", "表达式", "0/10 * * * * ?"),
                Map.of("说明", "每分钟", "表达式", "0 * * * * ?"),
                Map.of("说明", "每5分钟", "表达式", "0 0/5 * * * ?"),
                Map.of("说明", "早上8点", "表达式", "0 0 8 * * ?"),
                Map.of("说明", "工作日9点", "表达式", "0 0 9 ? * MON-FRI")
        );
    }

    @Operation(summary = "查看任务执行记录")
    @GetMapping("/results")
    public Map<String, Object> results() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("activeTasks", scheduledTaskService.getActiveTaskIds());
        result.put("executionRecords", scheduledTaskService.getExecutionRecords());
        result.put("overview", scheduledTaskService.getOverview());
        return result;
    }

    @Operation(summary = "取消指定任务")
    @DeleteMapping("/cancel")
    public Map<String, Object> cancel(@RequestParam String taskId) {
        boolean ok = scheduledTaskService.cancelTask(taskId);
        return Map.of("status", ok ? "ok" : "not_found", "taskId", taskId, "cancelled", ok);
    }

    @Operation(summary = "取消所有任务")
    @DeleteMapping("/cancel-all")
    public Map<String, Object> cancelAll() {
        int count = scheduledTaskService.cancelAllTasks();
        return Map.of("status", "ok", "cancelledCount", count);
    }

    @Operation(summary = "定时任务概览")
    @GetMapping("/overview")
    public Map<String, Object> overview() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("title", "RScheduledExecutorService 分布式定时任务");
        result.put("comparison", Map.of(
                "@Scheduled", "单机, 多实例重复 → 需加分布式锁",
                "XXL-Job/Quartz", "重量级, 独立调度中心+DB",
                "RScheduledExecutorService", "轻量分布式, 基于Redis, 无额外依赖"
        ));
        result.put("features", List.of(
                "✅ 分布式: 集群中仅一个节点执行",
                "✅ 动态: 运行时新增/取消, 无需重启",
                "✅ 故障转移: 节点宕机自动迁移",
                "✅ 持久化: 任务保存在Redis"
        ));
        return result;
    }
}
