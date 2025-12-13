package com.github.axinger._11utils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//使用固定大小为 10 的线程池可以间接控制并发量为 10。
//
//CompletableFuture 结合线程池可以实现异步任务的并发控制。
//
//这种方式简单且高效，适合大多数并发任务场景。
public class CompletableFutureWithThreadPool {
    public static void main(String[] args) throws InterruptedException {
        // 创建固定大小为10的线程池
        ExecutorService executor = Executors.newFixedThreadPool(10);

        // 总任务数
        int totalTasks = 100;
        // 用于等待所有任务完成
        CountDownLatch allTasksDone = new CountDownLatch(totalTasks);

        // 提交100个任务
        for (int i = 0; i < totalTasks; i++) {
            int taskId = i;
            CompletableFuture.runAsync(() -> {
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
            }, executor);
        }

        // 等待所有任务完成
        allTasksDone.await();
        System.out.println("All tasks are completed!");

        // 关闭线程池
        executor.shutdown();
    }
}
