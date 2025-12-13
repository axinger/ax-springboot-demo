package com.github.axinger._05queue;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;


public class QueueDemo {


    public static void main(String[] args) throws InterruptedException {


        // 链表
//        LinkedBlockingQueue linkedBlockingQueue =new LinkedBlockingQueue<>(10);


//        ArrayBlockingQueue_add_remove();

//        ArrayBlockingQueue_offer_poll();

//        ArrayBlockingQueue_put_take();

        ArrayBlockingQueue_offer_poll_time();
    }


    static void ArrayBlockingQueue_add_remove() {
        // ArrayBlockingQueue 固定长度,常用的,有界的
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(3);

        System.out.println("queue.add = " + queue.add("a"));
        System.out.println("queue.add = " + queue.add("b"));
        System.out.println("queue.add = " + queue.add("c"));
        /// 检查 未取时,总是返回栈顶数据
        System.out.println("queue.element = " + queue.element());
        System.out.println("queue.element = " + queue.element());
        /// 阻塞,抛出异常
//        System.out.println("queue.add = " + queue.add("d"));

        System.out.println("queue.remove = " + queue.remove());
        System.out.println("queue.element = " + queue.element());
        System.out.println("queue.add = " + queue.add("d"));
    }

    static void ArrayBlockingQueue_offer_poll() {
        // ArrayBlockingQueue 固定长度,常用的,有界的
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(3);

        /// 不抛出异常,offer 返回是否成功
        System.out.println("queue.add = " + queue.offer("a"));
        System.out.println("queue.add = " + queue.offer("b"));
        System.out.println("queue.add = " + queue.offer("c"));
        System.out.println("queue.add = " + queue.offer("d"));


        System.out.println("queue.poll = " + queue.poll());
        System.out.println("queue.poll = " + queue.poll());
        System.out.println("queue.poll = " + queue.poll());
        System.out.println("queue.poll = " + queue.poll());

    }

    static void ArrayBlockingQueue_put_take() throws InterruptedException {
        // ArrayBlockingQueue 固定长度,常用的,有界的
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(3);


        /// put take 阻塞,挂起程序,不停止,必须等取值
        queue.put("a");
        queue.put("b");
        queue.put("c");
//        queue.put("d");


        System.out.println("queue.take = " + queue.take());
        System.out.println("queue.take = " + queue.take());
        System.out.println("queue.take = " + queue.take());
//        System.out.println("queue.take = " + queue.take());

    }

    static void ArrayBlockingQueue_offer_poll_time() throws InterruptedException {
        // ArrayBlockingQueue 固定长度,常用的,有界的
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(3);


        /// offer poll  设置超时时间
        queue.offer("a");
        queue.offer("b");
        queue.offer("c");
//        queue.offer("d",3L, TimeUnit.SECONDS);


        System.out.println("queue.take = " + queue.poll());
        System.out.println("queue.take = " + queue.poll());
        System.out.println("queue.take = " + queue.poll());
        System.out.println("queue.take = " + queue.poll(3, TimeUnit.SECONDS));


    }

    //put() 和 take() 是阻塞式的，适合用于协调生产者和消费者节奏。
    @SneakyThrows
    @Test
    public void test10() {

        ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(3);

        Thread producer = new Thread(new Producer(queue));
        Thread consumer = new Thread(new Consumer(queue));

        producer.start();
        consumer.start();

        TimeUnit.SECONDS.sleep(10);
    }

    class Producer implements Runnable {
        private ArrayBlockingQueue<Integer> queue;

        public Producer(ArrayBlockingQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 5; i++) {
                    queue.put(i);
                    System.out.println("Produced: " + i);
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    class Consumer implements Runnable {
        private ArrayBlockingQueue<Integer> queue;

        public Consumer(ArrayBlockingQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 5; i++) {
                    Integer value = queue.take();
                    System.out.println("Consumed: " + value);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
