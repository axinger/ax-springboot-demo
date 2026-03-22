package com.github.axinger._04readwritelock;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

/**
 * StampedLock 测试类
 * StampedLock 是 Java 8 引入的读写锁，提供了比 ReentrantReadWriteLock 更好的性能
 * 主要特点：
 * 1. 三种访问模式：读锁、写锁、乐观读
 * 2. 支持锁升级和降级
 * 3. 使用 stamp（时间戳）来管理锁状态
 * 4. 不支持重入
 * 5. 不支持条件变量
 */
public class StampedLockTest {

    /**
     * StampedLock 基本使用 - 缓存示例
     */
    @Test
    public void testBasicUsage() throws InterruptedException {
        StampedLock lock = new StampedLock();

        // 写操作
        long writeStamp = lock.writeLock();
        try {
            System.out.println(Thread.currentThread().getName() + " 获取写锁，stamp: " + writeStamp);
            TimeUnit.MILLISECONDS.sleep(100);
        } finally {
            lock.unlockWrite(writeStamp);
            System.out.println(Thread.currentThread().getName() + " 释放写锁");
        }
    }

    /**
     * StampedLock 读锁示例
     */
    @Test
    public void testReadLock() throws InterruptedException {
        StampedLock lock = new StampedLock();

        // 读操作
        long readStamp = lock.readLock();
        try {
            System.out.println(Thread.currentThread().getName() + " 获取读锁，stamp: " + readStamp);
            TimeUnit.MILLISECONDS.sleep(100);
        } finally {
            lock.unlockRead(readStamp);
            System.out.println(Thread.currentThread().getName() + " 释放读锁");
        }
    }

    /**
     * StampedLock 乐观读示例
     * 乐观读不获取锁，但通过 validate 方法验证数据一致性
     */
    @Test
    public void testOptimisticRead() {
        StampedLock lock = new StampedLock();

        // 乐观读 - 不阻塞写操作
        long optimisticStamp = lock.tryOptimisticRead();
        try {
            System.out.println(Thread.currentThread().getName() + " 乐观读，stamp: " + optimisticStamp);
            // 读取数据后需要验证 stamp 是否有效
            if (!lock.validate(optimisticStamp)) {
                // 如果验证失败，说明在读取过程中有写操作，需要重新获取读锁
                System.out.println("乐观读验证失败，升级为读锁");
                long readStamp = lock.readLock();
                try {
                    // 重新读取数据
                    System.out.println("使用读锁重新读取数据");
                } finally {
                    lock.unlockRead(readStamp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * StampedLock 使用示例 - 缓存实现
     */
    @Test
    public void testCacheWithStampedLock() throws InterruptedException {
        StampedCache stampedCache = new StampedCache();

        // 写线程
        Thread writer = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                stampedCache.put("key" + i, "value" + i);
            }
        }, "Writer");

        // 读线程
        Thread reader1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                Object value = stampedCache.get("key" + i);
                System.out.println(Thread.currentThread().getName() + " 读取: key" + i + " = " + value);
            }
        }, "Reader-1");

        Thread reader2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                Object value = stampedCache.get("key" + i);
                System.out.println(Thread.currentThread().getName() + " 读取: key" + i + " = " + value);
            }
        }, "Reader-2");

        writer.start();
        reader1.start();
        reader2.start();

        writer.join();
        reader1.join();
        reader2.join();

        System.out.println("测试完成");
    }

    /**
     * StampedLock 锁升级示例
     * 从读锁升级到写锁
     */
    @Test
    public void testLockUpgrade() throws InterruptedException {
        StampedLock lock = new StampedLock();

        // 先获取读锁
        long readStamp = lock.readLock();
        try {
            System.out.println("获取读锁，stamp: " + readStamp);

            // 尝试将读锁升级为写锁
            long writeStamp = lock.tryConvertToWriteLock(readStamp);
            if (writeStamp != 0) {
                System.out.println("成功升级为写锁，stamp: " + writeStamp);
                try {
                    // 写操作
                    System.out.println("执行写操作");
                } finally {
                    lock.unlockWrite(writeStamp);
                }
            } else {
                // 升级失败，需要释放读锁再获取写锁
                System.out.println("升级失败，释放读锁后重新获取写锁");
                lock.unlockRead(readStamp);
                writeStamp = lock.writeLock();
                try {
                    System.out.println("执行写操作");
                } finally {
                    lock.unlockWrite(writeStamp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * StampedLock 锁降级示例
     * 从写锁降级为读锁
     */
    @Test
    public void testLockDowngrade() throws InterruptedException {
        StampedLock lock = new StampedLock();

        // 先获取写锁
        long writeStamp = lock.writeLock();
        try {
            System.out.println("获取写锁，stamp: " + writeStamp);

            // 将写锁降级为读锁
            long readStamp = lock.tryConvertToReadLock(writeStamp);
            if (readStamp != 0) {
                System.out.println("成功降级为读锁，stamp: " + readStamp);
                try {
                    // 读操作
                    System.out.println("执行读操作");
                } finally {
                    lock.unlockRead(readStamp);
                }
            } else {
                // 降级失败
                System.out.println("降级失败，直接释放写锁");
                lock.unlockWrite(writeStamp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * StampedLock 多线程并发测试
     */
    @Test
    public void testConcurrentAccess() throws InterruptedException {
        StampedCache cache = new StampedCache();
        int threadCount = 10;
        int operationCount = 100;

        // 创建多个线程并发操作缓存
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < operationCount; j++) {
                    String key = "key" + (j % 10);
                    if (threadId % 2 == 0) {
                        // 偶数线程执行写操作
                        cache.put(key, "value-" + threadId + "-" + j);
                    } else {
                        // 奇数线程执行读操作
                        cache.get(key);
                    }
                }
            }, "Thread-" + threadId);
        }

        // 启动所有线程
        for (Thread thread : threads) {
            thread.start();
        }

        // 等待所有线程完成
        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("并发测试完成");
    }
}

/**
 * 使用 StampedLock 实现的线程安全缓存
 */
class StampedCache {
    private final Map<String, Object> map = new HashMap<>();
    private final StampedLock lock = new StampedLock();

    /**
     * 写入数据 - 使用写锁
     */
    public void put(String key, Object value) {
        long writeStamp = lock.writeLock();
        try {
            System.out.println(Thread.currentThread().getName() + " 正在写: " + key);
            TimeUnit.MICROSECONDS.sleep(100);
            map.put(key, value);
            System.out.println(Thread.currentThread().getName() + " 写完成: " + key);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlockWrite(writeStamp);
        }
    }

    /**
     * 读取数据 - 使用乐观读，如果失败则升级为读锁
     */
    public Object get(String key) {
        // 1. 尝试乐观读
        long optimisticStamp = lock.tryOptimisticRead();
        try {
            System.out.println(Thread.currentThread().getName() + " 乐观读: " + key);
            Object value = map.get(key);

            // 2. 验证乐观读是否有效
            if (lock.validate(optimisticStamp)) {
                // 验证成功，直接返回
                return value;
            }

            // 3. 验证失败，升级为读锁
            System.out.println(Thread.currentThread().getName() + " 乐观读失败，升级为读锁");
            long readStamp = lock.readLock();
            try {
                return map.get(key);
            } finally {
                lock.unlockRead(readStamp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取缓存大小 - 使用读锁
     */
    public int size() {
        long readStamp = lock.readLock();
        try {
            return map.size();
        } finally {
            lock.unlockRead(readStamp);
        }
    }
}