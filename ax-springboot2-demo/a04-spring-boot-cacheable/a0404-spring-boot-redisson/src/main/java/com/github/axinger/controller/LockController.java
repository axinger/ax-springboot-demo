package com.github.axinger.controller;

import com.github.axinger.service.LockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 分布式锁 Controller — Redisson 锁体系
 *
 * <pre>
 * /lock/lock        — 可重入锁 (看门狗自动续期)
 * /lock/tryLock     — 尝试加锁
 * /lock/write       — 写锁
 * /lock/read        — 读锁
 * /lock/semaphore   — 信号量
 * /lock/secKill     — 秒杀 (单次)
 * /lock/secKill/concurrent — 秒杀 (多线程压测)
 * </pre>
 */
@Tag(name = "分布式锁Demo", description = "Redisson 锁: 可重入锁 + 读写锁 + 信号量 + 秒杀实战")
@RestController
@RequestMapping("/lock")
public class LockController {

    @Resource
    private LockService lockService;

    // ==================== 1. RLock 可重入锁 ====================

    @Operation(summary = "阻塞加锁", description = "lock() — 看门狗自动续期30s, 业务完成后手动释放")
    @GetMapping("/lock")
    public Map<String, Object> lock(@RequestParam(defaultValue = "myLock") String key) {
        String result = lockService.lockAndExecute(key);
        return Map.of("status", "ok", "type", "RLock.lock()", "result", result,
                "watchdog", "✅ 看门狗自动续期30s");
    }

    @Operation(summary = "尝试加锁", description = "tryLock(waitTime, leaseTime) — 指定等待和持有时间")
    @GetMapping("/tryLock")
    public Map<String, Object> tryLock(@RequestParam(defaultValue = "myLock") String key,
                                        @RequestParam(defaultValue = "3") long waitSeconds) {
        String result = lockService.tryLock(key, waitSeconds, 10);
        return Map.of("status", "ok", "type", "RLock.tryLock()", "result", result);
    }

    // ==================== 2. RReadWriteLock 读写锁 ====================

    @Operation(summary = "写锁", description = "写+写阻塞, 读+写阻塞")
    @GetMapping("/write")
    public Map<String, Object> write(@RequestParam(defaultValue = "rwKey") String key,
                                      @RequestParam(defaultValue = "value") String value) {
        String result = lockService.writeValue(key, value);
        return Map.of("status", "ok", "type", "RReadWriteLock.writeLock()", "result", result);
    }

    @Operation(summary = "读锁", description = "读+读=无锁(并发), 写+读=阻塞")
    @GetMapping("/read")
    public Map<String, Object> read(@RequestParam(defaultValue = "rwKey") String key) {
        String result = lockService.readValue(key);
        return Map.of("status", "ok", "type", "RReadWriteLock.readLock()", "result", result);
    }

    // ==================== 3. RSemaphore 信号量 ====================

    @Operation(summary = "初始化信号量", description = "设置可用许可证数量")
    @GetMapping("/semaphore/init")
    public Map<String, Object> semaphoreInit(@RequestParam(defaultValue = "park") String key,
                                              @RequestParam(defaultValue = "3") int permits) {
        lockService.initSemaphore(key, permits);
        return Map.of("status", "ok", "key", key, "permits", permits, "desc", "停车场有" + permits + "个车位");
    }

    @Operation(summary = "获取信号量(停车)", description = "tryAcquire() — 非阻塞, 无车位立即返回")
    @GetMapping("/semaphore/acquire")
    public Map<String, Object> semaphoreAcquire(@RequestParam(defaultValue = "park") String key) {
        String result = lockService.acquireSemaphore(key);
        return Map.of("status", "ok", "result", result);
    }

    @Operation(summary = "释放信号量(离开)", description = "release() — 释放一个许可证")
    @GetMapping("/semaphore/release")
    public Map<String, Object> semaphoreRelease(@RequestParam(defaultValue = "park") String key) {
        String result = lockService.releaseSemaphore(key);
        return Map.of("status", "ok", "result", result);
    }

    // ==================== 4. 秒杀 ====================

    @Operation(summary = "初始化秒杀库存")
    @GetMapping("/secKill/init")
    public Map<String, Object> secKillInit(@RequestParam(defaultValue = "101") Integer prodId,
                                            @RequestParam(defaultValue = "100") Integer count) {
        lockService.initStock(prodId, count);
        return Map.of("status", "ok", "prodId", prodId, "stock", count);
    }

    @Operation(summary = "查询库存")
    @GetMapping("/secKill/stock")
    public Map<String, Object> secKillStock(@RequestParam(defaultValue = "101") Integer prodId) {
        return Map.of("prodId", prodId, "stock", lockService.getStock(prodId));
    }

    @Operation(summary = "秒杀(单次)", description = "分布式锁保证库存扣减 + 用户去重")
    @GetMapping("/secKill")
    public Map<String, Object> secKill(@RequestParam(defaultValue = "101") Integer prodId,
                                        @RequestParam(defaultValue = "1") Integer userId) {
        return lockService.secKill(userId, prodId);
    }

    @Operation(summary = "秒杀(多线程压测)", description = "模拟N个用户并发秒杀, 验证分布式锁正确性")
    @GetMapping("/secKill/concurrent")
    public Map<String, Object> secKillConcurrent(@RequestParam(defaultValue = "101") Integer prodId,
                                                  @RequestParam(defaultValue = "300") int userCount,
                                                  @RequestParam(defaultValue = "100") int stockCount) {
        return lockService.secKillConcurrent(prodId, userCount, stockCount);
    }

    // ==================== 概览 ====================

    @Operation(summary = "锁系统概览")
    @GetMapping("/overview")
    public Map<String, Object> overview() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("title", "Redisson 分布式锁体系");

        result.put("locks", List.of(
                Map.of("type", "RLock", "feature", "可重入锁+看门狗", "bestFor", "通用互斥场景",
                        "key", "lock.lock() 自动续期 / lock.tryLock() 带超时"),
                Map.of("type", "RReadWriteLock", "feature", "读写锁", "bestFor", "读多写少场景",
                        "key", "读+读并发, 写+读/写+写互斥"),
                Map.of("type", "RSemaphore", "feature", "信号量", "bestFor", "限流/停车位/连接池",
                        "key", "tryAcquire() 非阻塞获取"),
                Map.of("type", "秒杀实战", "feature", "分布式锁+Redis库存", "bestFor", "高并发抢购",
                        "key", "锁保证 查库存→扣库存→记录用户 的原子性")
        ));

        result.put("tips", List.of(
                "✅ 看门狗: lock() 不加参数, 默认30s自动续期, 业务完成自动停止",
                "✅ 指定时间: lock(10, SECONDS) 不会自动续期, 10s后自动释放",
                "✅ tryLock: 可指定等待时间和持有时间, 推荐用于秒杀等高并发场景",
                "⚠️ unlock: 务必在 finally 中释放"
        ));

        return result;
    }
}
