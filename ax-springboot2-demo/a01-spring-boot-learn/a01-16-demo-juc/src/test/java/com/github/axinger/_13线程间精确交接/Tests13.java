package com.github.axinger._13线程间精确交接;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

public class Tests13 {

    // SynchronousQueue 是一种特殊且高效的线程间通信机制。与传统的阻塞队列不同，
    // SynchronousQueue 不存储元素，而是直接在生产者和消费者线程之间传递数据。这种设计使其成为实现严格线程间同步和精确任务交接的理想工具。
//    SynchronousQueue 是 Java 并发包中一个特殊的阻塞队列实现。它的核心特性是：
//    无内部容量：不存储任何元素，size() 方法始终返回 0
//    直接传递：元素从生产者线程直接传递给消费者线程
//    严格同步：生产者和消费者操作必须在时间上精确同步
    @SneakyThrows
    @Test
    public void test1()  {

        ExecutorService executor = Executors.newFixedThreadPool(2);
        SynchronousQueue<Integer> queue = new SynchronousQueue<>();

        // 生产者线程
        Runnable producer = () -> {
            Integer producedElement = ThreadLocalRandom.current().nextInt();
            try {
                queue.put(producedElement);
                System.out.println("生产者线程element: " + producedElement + " to the exchange point");
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                System.err.println("生产者线程,中断"+ex);
            }
        };

        // 消费者线程
        Runnable consumer = () -> {
            try {
                Integer consumedElement = queue.take();
                System.out.println("消费者线程element: " + consumedElement + " from the exchange point");
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                System.err.println("消费者线程,中断"+ex);
            }
        };

        executor.execute(producer);
        executor.execute(consumer);

        executor.awaitTermination(500, TimeUnit.MILLISECONDS);
        executor.shutdown();
    }
}
