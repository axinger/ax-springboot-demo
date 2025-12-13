package com.github.axinger._02lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ShareResource {

    private final Lock lock = new ReentrantLock();
    // 条件锁
    private final Condition conditionA1 = lock.newCondition();
    private final Condition conditionA2 = lock.newCondition();
    private final Condition conditionA3 = lock.newCondition();
    // 定义标志位
    private Integer flag = 1;// 1 A1, 2 A2,3 A3

    public void log5(int loop) throws InterruptedException {
        lock.lock();
        try {
            while (flag != 1) {
                conditionA1.await();
            }
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + "::" + i);
            }
            // 修改flag
            flag = 2;
            // 通知下一个
            conditionA2.signal();
        } finally {
            lock.unlock();
        }

    }

    public void log10(int loop) throws InterruptedException {
        lock.lock();
        try {
            while (flag != 2) {
                conditionA2.await();
            }
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + "::" + i);
            }
            // 修改flag
            flag = 3;
            // 通知下一个
            conditionA3.signal();
        } finally {
            lock.unlock();
        }

    }

    public void log15(int loop) throws InterruptedException {
        lock.lock();
        try {
            while (flag != 3) {
                conditionA3.await();
            }
            for (int i = 0; i < 15; i++) {
                System.out.println(Thread.currentThread().getName() + "::" + i);
            }
            flag = 1;
            // 通知下一个
            conditionA1.signal();
        } finally {
            lock.unlock();
        }

    }

}

/**
 * 多线程 定制通信
 */
public class ThreadDemo2 {

    public static void main(String[] args) {


        ShareResource shareResource = new ShareResource();

        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    shareResource.log5(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "A1").start();


        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    shareResource.log10(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "A2").start();


        new Thread(() -> {
            for (int i = 0; i < 15; i++) {
                try {
                    shareResource.log15(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "A3").start();
    }
}
