package com.github.axinger._01sync;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 可重入锁
 */
public class ThreadDemo2 {

    public static void main(String[] args) {

        // 递归锁,溢出异常,因为是可重复锁
//        new ThreadDemo2().add();


//        Object o = new Object();
//
//        new Thread(() -> {
//            synchronized (o) {
//                System.out.println(Thread.currentThread().getName() + "外层");
//                synchronized (o) {
//                    System.out.println(Thread.currentThread().getName() + "中层");
//                    synchronized (o) {
//                        System.out.println(Thread.currentThread().getName() + "内层");
//                    }
//                }
//            }
//        }, "A1").start();


        // 可重入锁
        final Lock lock = new ReentrantLock(true);

        new Thread(() -> {
            try {
                lock.lock();
                System.out.println(Thread.currentThread().getName() + "外层");
                try {
                    lock.lock();
                    System.out.println(Thread.currentThread().getName() + "内层");
                } finally {
                    lock.unlock();
                }
            } finally {
                lock.unlock();
            }
        }, "A1").start();
    }

    public synchronized void add() {
        add();
    }


}
