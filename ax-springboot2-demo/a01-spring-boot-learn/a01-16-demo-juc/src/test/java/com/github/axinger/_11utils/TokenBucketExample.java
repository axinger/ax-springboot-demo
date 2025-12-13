package com.github.axinger._11utils;

import org.springframework.util.StopWatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

//为什么会有 Task 99 is running=Thread-99？
//
//虽然 Semaphore 限制了最多 10 个任务同时执行，但线程的总数仍然是 100 个。
public class TokenBucketExample {
    public static void main(String[] args) throws InterruptedException {
        // 创建计时器
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();

        // 令牌桶大小为10
        Semaphore semaphore = new Semaphore(10);

        // 总任务数
        int totalTasks = 100;
        // 用于等待所有任务完成
        CountDownLatch allTasksDone = new CountDownLatch(totalTasks);

        // 模拟100个任务
        for (int i = 0; i < totalTasks; i++) {
            new Thread(new Task(semaphore, i, allTasksDone)).start();
        }

        // 等待所有任务完成
        allTasksDone.await();

        // 停止计时器
        stopwatch.stop();
        System.out.println("任务耗时=" + stopwatch.getTotalTimeSeconds() + "秒");
    }

    static class Task implements Runnable {
        private final Semaphore semaphore;
        private final int taskId;
        private final CountDownLatch allTasksDone;

        public Task(Semaphore semaphore, int taskId, CountDownLatch allTasksDone) {
            this.semaphore = semaphore;
            this.taskId = taskId;
            this.allTasksDone = allTasksDone;
        }

        @Override
        public void run() {
            try {
                // 获取令牌
                semaphore.acquire();
                System.out.println("Task " + taskId + " is running=" + Thread.currentThread().getName());

                // 模拟任务执行时间
                Thread.sleep(1000);

                System.out.println("Task " + taskId + " is completed");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // 释放令牌
                semaphore.release();
                // 通知任务完成
                allTasksDone.countDown();
            }
        }
    }
}
