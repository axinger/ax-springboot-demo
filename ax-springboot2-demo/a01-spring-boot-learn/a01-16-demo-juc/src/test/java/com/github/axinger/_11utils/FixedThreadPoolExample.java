package com.github.axinger._11utils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

//使用 Executors.newFixedThreadPool(10) 可以限制线程的总数为 10。
//
//提交 100 个任务时，线程池会依次执行这些任务，最多同时执行 10 个任务。
//
//通过 CountDownLatch 可以等待所有任务完成。
//
//这种方式既控制了线程数量，又保证了任务的并发执行。
public class FixedThreadPoolExample {
    public static void main(String[] args) throws InterruptedException {
        // 创建固定大小为10的线程池
        ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(10);

        // 总任务数
        int totalTasks = 100;
        // 用于等待所有任务完成
        CountDownLatch allTasksDone = new CountDownLatch(totalTasks);

        // 提交100个任务
        for (int i = 0; i < totalTasks; i++) {
            int taskId = i;
            executor.submit(() -> {
                try {
                    System.out.println("Task " + taskId + " is running on " + Thread.currentThread().getName());
                    // 模拟任务执行时间
                    Thread.sleep(1000);
                    System.out.println("Task " + taskId + " is completed");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // 通知任务完成
                    allTasksDone.countDown();
                }
            });
        }

        // 等待所有任务完成
        allTasksDone.await();
        System.out.println("All tasks are completed!");

        // 关闭线程池
        executor.shutdown();
    }
}
