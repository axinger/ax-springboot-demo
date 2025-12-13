package com.github.axinger._02lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Share {
    private final Lock lock = new ReentrantLock();
    // 条件锁
    private final Condition condition = lock.newCondition();
    private Integer num = 0;

    //+1
    void incr() throws InterruptedException {

        // 加锁
        lock.lock();
        try {

            // 防止虚假唤醒,用while
            while (num != 0) {
                condition.await();
            }
            num++;
            System.out.println("lock方式: num = " + num + "::" + Thread.currentThread().getName());
            // 通知其他线程
            condition.signalAll();
        } finally {
            // 解锁
            lock.unlock();
        }
    }

    // -1
    synchronized void decr() throws InterruptedException {

        lock.lock();
        try {

            // 防止虚假唤醒,用while
            while (num != 1) {
                condition.await();
            }
            num--;
            System.out.println("lock方式: num = " + num + "::" + Thread.currentThread().getName());
            // 通知其他线程
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

}

public class ThreadDemo1 {

    /**
     * 线程间通信,通知
     */
    public static void main(String[] args) {
        Share share = new Share();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    share.incr();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "incr_1").start();


        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    share.decr();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "decr_1").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    share.incr();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "incr_2").start();


        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    share.decr();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "decr_2").start();

    }

}
