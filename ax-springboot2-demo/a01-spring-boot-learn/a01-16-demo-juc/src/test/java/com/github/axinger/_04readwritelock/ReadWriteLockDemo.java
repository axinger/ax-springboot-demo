package com.github.axinger._04readwritelock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class MyCache {

    /***
     * volatile 表示
     * volatile原理：
     *
     * 被volatile关键字修饰的变量，编译器与运行时都会注意到这个变量是共享的，
     * 因此不会将该变量上的操作与其他内存操作一起重排序。
     * volatile变量不会被缓存在寄存器或者对其他处理器不可见的地方，
     * 因此在读取volatile类型的变量时总会返回最新写入的值。
     */
    private final Map<String, Object> map = new HashMap<>();

    /**
     * 创建读写锁
     */
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();


    // set数据
    public void put(String key, Object value) {

        // 添加写锁
        readWriteLock.writeLock().lock();

        try {

            System.out.println(Thread.currentThread().getName() + "正在写操作" + key);
            TimeUnit.MICROSECONDS.sleep(300);
            map.put(key, value);
            System.out.println(Thread.currentThread().getName() + "写完了" + key);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 释放写锁
            readWriteLock.writeLock().unlock();
        }
    }


    public Object get(String key) {

        // 添加读锁
        readWriteLock.readLock().lock();
        Object o = null;
        try {
            System.out.println(Thread.currentThread().getName() + "正在读操作" + key);
            TimeUnit.MICROSECONDS.sleep(300);

            o = map.get(key);
            System.out.println(Thread.currentThread().getName() + "读完了" + key);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 释放读锁
            readWriteLock.readLock().unlock();
        }
        return o;
    }
}

public class ReadWriteLockDemo {


    public static void main(String[] args) throws InterruptedException {
        MyCache myCache = new MyCache();

        // 写数据
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            new Thread(() -> {
                myCache.put(String.valueOf(finalI), finalI + "");
            }, String.valueOf(i)).start();
        }

        TimeUnit.MICROSECONDS.sleep(300);

        // 读数据
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            new Thread(() -> {
                myCache.get(String.valueOf(finalI));
            }, String.valueOf(i)).start();
        }

    }
}


/**
 * 读写锁降级
 */
class ReadWriteLockDemo2 {
    public static void main(String[] args) {


        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();

        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();


        // 锁降级
        readLock.lock();
        System.out.println("writeLock==================");

        writeLock.lock();
        System.out.println("readLock==================");

        // 降锁,才能完成上面
        writeLock.unlock();
        readLock.unlock();


    }
}
