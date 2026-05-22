package com.github.axinger.controller;

import lombok.SneakyThrows;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 使用分布式锁,进行秒杀
 */
@RestController
public class RedissonLockController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Autowired
    private RedissonClient redissonClient;

    @GetMapping(value = "/add")
    public String add() {
        redisTemplate.opsForValue().set("phone", 100);
        Object phone = redisTemplate.opsForValue().get("phone");
        return String.valueOf(phone);
    }


    @GetMapping(value = "/buy2")
    public String get() {
        String result = null;

        RLock lock = redissonClient.getLock("phoneLock");
        lock.lock(3, TimeUnit.SECONDS);
        try {
            Integer phone = (Integer) redisTemplate.opsForValue().get("phone");
            Integer count = phone;

            if (count > 0) {
//                redisTemplate.opsForValue().set("phone", String.valueOf(count - 1));
                redisTemplate.opsForValue().decrement("phone", 1);
                System.out.println("抢到了" + count + "号商品");
                result = "抢到了" + count + "号商品";
            } else {
                result = "库存没有了.............";
            }

        } finally {
            System.out.println("result = " + result);
            lock.unlock();
        }
        return result;
    }


    @SneakyThrows
    @GetMapping(value = "/hello")
    public Object hello() {
        RLock lock = redissonClient.getLock("phoneLock");

        // 1.加锁
        // 1.1 锁的自动续期,业务超长,会自动续期30秒,不用担心业务厂,锁被删除
        // 1.2 加锁业务完成,就不会自动续期,即使不解锁,30秒后也会删除锁
//          lock.lock(); // 阻塞式等待
//        lock.lock(10, TimeUnit.SECONDS); //10秒后自动解锁,自动解锁时间要大于业务执行时间,不然会误删别人的锁
        final boolean tryLock = lock.tryLock(10, TimeUnit.SECONDS);

        if (tryLock) {
            try {

                // 2.处理业务

            } finally {
                // 3.解锁,多个业务用一个锁时,有个业务终端,没有解锁,不会死锁
                // 有个 watchDog 看门狗,自动续期
                lock.unlock();
            }
        }
        return "hello";
    }

    // 读写锁
    // 读+读,相当于无所
    // 写 + 读,读必须等写锁释放
    // 写+写,阻塞
    // 读 + 写,
    @GetMapping(value = "/write")
    public Object write() {
        RReadWriteLock lock = redissonClient.getReadWriteLock("rw-lock");
        String uuid = "";

        // 1.改数据,加写锁
        final RLock writeLock = lock.writeLock();
        try {
            writeLock.lock();
            uuid = UUID.randomUUID().toString();
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
        redisTemplate.opsForValue().set("writeLockValue", uuid);
        return uuid;
    }


    @GetMapping(value = "/read")
    public Object read() {

        RReadWriteLock lock = redissonClient.getReadWriteLock("rw-lock");
        final RLock readLock = lock.readLock();
        readLock.lock();
        String uuid = "";
        try {
            uuid = (String) redisTemplate.opsForValue().get("writeLockValue");
        } finally {
            readLock.unlock();
        }
        return uuid;
    }

    @GetMapping(value = "/parkNo")
    public Object parkNo() {
        // 初始化3个停车位
        redisTemplate.opsForValue().set("park", 3);
        return "ok";
    }

    /// 停车位 3个停车位
    @GetMapping(value = "/park")
    public Object park() {
        RSemaphore semaphore = redissonClient.getSemaphore("park");
//        try {
//            semaphore.acquire();// 获取一个信号
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        // 不会阻塞
        final boolean tryAcquire = semaphore.tryAcquire();

        return tryAcquire ? "ok" : "not ok";
    }

    @GetMapping(value = "/go")
    public Object go() {
        RSemaphore semaphore = redissonClient.getSemaphore("park");
        semaphore.release();// 释放一个信号

        return "ok";
    }
}

