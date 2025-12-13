package com.github.axinger._11utils;

import org.springframework.util.StopWatch;

import java.util.concurrent.*;

public class CompletableFutureExample {
    public static void main(String[] args) throws InterruptedException {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();


        // 创建 Semaphore，限制并发量为 10
        Semaphore semaphore = new Semaphore(2);

        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(5);

        // 总任务数
        int totalTasks = 10;
        // 用于等待所有任务完成
        CountDownLatch allTasksDone = new CountDownLatch(totalTasks);

        // 提交 100 个任务
        for (int i = 0; i < totalTasks; i++) {
            int taskId = i;
            CompletableFuture.runAsync(() -> {
                try {
                    // 获取信号量，限制并发量
                    semaphore.acquire();
                    System.out.println("Task " + taskId + " is running on " + Thread.currentThread().getName());

                    // 模拟任务执行时间
                    Thread.sleep(1000);

//                    System.out.println("Task " + taskId + " is completed");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // 释放信号量
                    semaphore.release();
                    // 通知任务完成
                    allTasksDone.countDown();
                }
            }, executor);
        }

        // 等待所有任务完成
        allTasksDone.await();
        System.out.println("All tasks are completed!");

        // 关闭线程池
        executor.shutdown();

        stopwatch.stop();
        System.out.println("任务耗时=" + stopwatch.getTotalTimeSeconds() + "秒");
    }
}
