package com.github.axinger._06pool;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 一池N线程
 * 一池一线程
 * 可扩容
 */
public class ThreadPoolDemo {
    public static void main(String[] args) {

//        test_newFixedThreadPool();
//        test_newSingleThreadExecutor();
        test_newCachedThreadPool();
    }

    static void test_newFixedThreadPool() {

        ExecutorService service = Executors.newFixedThreadPool(5);

        try {
            for (int i = 0; i < 10; i++) {
                service.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + " :办理业务");

                });

            }
        } finally {
            /// 关闭
            service.shutdown();
        }
    }

    /// 一池一线程
    static void test_newSingleThreadExecutor() {

        ExecutorService service = Executors.newSingleThreadExecutor();

        try {
            for (int i = 0; i < 10; i++) {
                service.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + " :办理业务");

                });

            }
        } finally {
            /// 关闭
            service.shutdown();
        }
    }

    /// 可扩容
    static void test_newCachedThreadPool() {

        ExecutorService service = Executors.newCachedThreadPool();

        try {
            for (int i = 0; i < 30; i++) {
                service.execute(() -> {

                    System.out.println(Thread.currentThread().getName() + " :办理业务");

                });

            }
        } finally {
            /// 关闭
            service.shutdown();
        }
    }

}
