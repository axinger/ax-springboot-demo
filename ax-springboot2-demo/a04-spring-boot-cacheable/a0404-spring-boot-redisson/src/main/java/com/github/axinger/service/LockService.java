package com.github.axinger.service;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁服务 — Redisson 锁 + 秒杀场景
 * <pre>
 * 1. RLock          — 可重入锁 (看门狗自动续期)
 * 2. RReadWriteLock — 读写锁
 * 3. RSemaphore     — 信号量
 * 4. 秒杀实战        — 分布式锁保证库存扣减原子性
 * </pre>
 */
@Slf4j
@Service
public class LockService {

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    // ==================== 1. RLock 可重入锁 ====================

    /**
     * 阻塞加锁 — 看门狗自动续期30s, 业务完成后自动释放
     */
    public String lockAndExecute(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        try {
            log.info("[Lock] 获得锁: {}", lockKey);
            TimeUnit.SECONDS.sleep(3); // 模拟业务
            return "执行成功, key=" + lockKey;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "被中断";
        } finally {
            lock.unlock();
            log.info("[Lock] 释放锁: {}", lockKey);
        }
    }

    /**
     * 尝试加锁 — 指定等待时间和持有时间
     */
    public String tryLock(String lockKey, long waitSeconds, long leaseSeconds) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            if (lock.tryLock(waitSeconds, leaseSeconds, TimeUnit.SECONDS)) {
                try {
                    log.info("[tryLock] 获得锁: {}", lockKey);
                    TimeUnit.SECONDS.sleep(2);
                    return "加锁成功, key=" + lockKey;
                } finally {
                    lock.unlock();
                }
            } else {
                return "加锁失败: 等待超时";
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "被中断";
        }
    }

    // ==================== 2. RReadWriteLock 读写锁 ====================

    public String writeValue(String key, String value) {
        RReadWriteLock rwLock = redissonClient.getReadWriteLock("rw:" + key);
        RLock writeLock = rwLock.writeLock();
        writeLock.lock(10, TimeUnit.SECONDS);
        try {
            redisTemplate.opsForValue().set(key, value);
            log.info("[写锁] key={}, value={}", key, value);
            return "写入成功: " + value;
        } finally {
            writeLock.unlock();
        }
    }

    public String readValue(String key) {
        RReadWriteLock rwLock = redissonClient.getReadWriteLock("rw:" + key);
        RLock readLock = rwLock.readLock();
        readLock.lock(10, TimeUnit.SECONDS);
        try {
            Object val = redisTemplate.opsForValue().get(key);
            log.info("[读锁] key={}, value={}", key, val);
            return "读取结果: " + val;
        } finally {
            readLock.unlock();
        }
    }

    // ==================== 3. RSemaphore 信号量 ====================

    public void initSemaphore(String key, int permits) {
        redisTemplate.opsForValue().set(key, permits);
        log.info("[信号量] 初始化: key={}, permits={}", key, permits);
    }

    public String acquireSemaphore(String key) {
        RSemaphore semaphore = redissonClient.getSemaphore(key);
        boolean acquired = semaphore.tryAcquire();
        return acquired ? "获取信号量成功, key=" + key : "信号量不足, key=" + key;
    }

    public String releaseSemaphore(String key) {
        RSemaphore semaphore = redissonClient.getSemaphore(key);
        semaphore.release();
        return "释放信号量成功";
    }

    // ==================== 4. 秒杀场景 ====================

    private String prodKey(Integer prodId) {
        return "sk:" + prodId + ":qt";
    }

    private String userKey(Integer prodId) {
        return "sk:" + prodId + ":users";
    }

    /** 初始化库存 */
    public Integer initStock(Integer prodId, Integer count) {
        redisTemplate.opsForValue().set(prodKey(prodId), count);
        redisTemplate.delete(userKey(prodId));
        log.info("[秒杀] 初始化库存: prodId={}, count={}", prodId, count);
        return count;
    }

    /** 查询库存 */
    public Integer getStock(Integer prodId) {
        Object val = redisTemplate.opsForValue().get(prodKey(prodId));
        if (val == null) return 0;
        return Integer.valueOf(val.toString());
    }

    /**
     * 秒杀 — 分布式锁保证原子性
     * 修复: 原代码 Integer.getInteger() bug — 改用 Integer.valueOf()
     */
    public Map<String, Object> secKill(Integer userId, Integer prodId) {
        Map<String, Object> result = new HashMap<>();
        RLock lock = redissonClient.getLock("lock:" + prodKey(prodId));
        lock.lock(3, TimeUnit.SECONDS);
        try {
            String pk = prodKey(prodId);
            String uk = userKey(prodId);

            // 1. 查库存 — 使用 toString 避免类型转换异常
            Object stockObj = redisTemplate.opsForValue().get(pk);
            if (stockObj == null) {
                result.put("success", false);
                result.put("msg", "秒杀未开始");
                return result;
            }
            int stock = Integer.valueOf(stockObj.toString());

            // 2. 判断库存
            if (stock <= 0) {
                result.put("success", false);
                result.put("msg", "库存不足");
                return result;
            }

            // 3. 判断是否重复秒杀
            Boolean isMember = redisTemplate.opsForSet().isMember(uk, userId.toString());
            if (Boolean.TRUE.equals(isMember)) {
                result.put("success", false);
                result.put("msg", "已秒杀过, 请勿重复");
                return result;
            }

            // 4. 扣库存 + 记录用户
            Long remaining = redisTemplate.opsForValue().decrement(pk, 1);
            redisTemplate.opsForSet().add(uk, userId.toString());

            result.put("success", true);
            result.put("msg", "秒杀成功!");
            result.put("userId", userId);
            result.put("remaining", remaining);
            log.info("[秒杀] 成功: userId={}, prodId={}, remaining={}", userId, prodId, remaining);

        } finally {
            lock.unlock();
        }
        return result;
    }

    /** 多线程模拟秒杀 */
    public Map<String, Object> secKillConcurrent(Integer prodId, int userCount, int stockCount) {
        // 初始化
        initStock(prodId, stockCount);

        Set<Integer> successUsers = Collections.synchronizedSet(new HashSet<>());

        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int i = 0; i < userCount; i++) {
            final int uid = i;
            futures.add(CompletableFuture.runAsync(() -> {
                Map<String, Object> r = secKill(uid, prodId);
                if (Boolean.TRUE.equals(r.get("success"))) {
                    successUsers.add(uid);
                }
            }));
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        Map<String, Object> result = new HashMap<>();
        result.put("prodId", prodId);
        result.put("stockInit", stockCount);
        result.put("userCount", userCount);
        result.put("successCount", successUsers.size());
        result.put("remainingStock", getStock(prodId));

        // 验证: 实际扣减与 set 中的用户数一致
        Object actualSetSize = redisTemplate.opsForSet().size(userKey(prodId));
        result.put("setSize", actualSetSize);
        return result;
    }
}
