package com.github.axinger;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("all")
public class 队列Tests {

    @Test
    public void test() throws InterruptedException {

        //移除并返回队头元素
//        Queue<Integer> queue = new LinkedList<>();
        Queue<Integer> queue = new LinkedList<>();
        boolean add = queue.add(1);
        queue.add(2);

//        add(E e): 将指定的元素插入到队列中。如果插入失败（例如队列已满），则抛出 IllegalStateException 异常。
//        offer(E e): 将指定的元素插入到队列中。如果插入失败，则返回 false，不会抛出异常。
        boolean offer = queue.offer(3);

        TimeUnit.SECONDS.sleep(1);

//        remove(): 移除并返回队列头部的元素。如果队列为空，则抛出 NoSuchElementException 异常。
//        poll(): 移除并返回队列头部的元素。如果队列为空，则返回 null，不会抛出异常。
//        输出：1
        System.out.println("remove1 = " + queue.remove());
        System.out.println("queue1 = " + queue);

        TimeUnit.SECONDS.sleep(1);
        System.out.println("remove2 = " + queue.remove());
        System.out.println("queue2 = " + queue);
    }

    @Test
    public void test2() {
        // 查看队列头部元素，但不移除
        Queue<Integer> queue = new LinkedList<>();
        queue.add(1);
        queue.add(2);
        int peek = queue.peek();
//        输出：1
        System.out.println("peek = " + peek);
        System.out.println("queue = " + queue);
    }

    @Test
    public void test3() {
        //队列判空
        Queue<Integer> queue = new LinkedList<>();
        boolean isEmpty = queue.isEmpty();
        System.out.println("isEmpty = " + isEmpty);
//        输出：true
    }


    @Test
    public void test4() {
        /// 根据元素的自然顺序或指定的比较器顺序对元素进行排序。
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.offer(3);
        pq.offer(1);
        pq.offer(2);
        System.out.println("pq = " + pq);

        System.out.println("优先级队列内容: " + pq);
        while (!pq.isEmpty()) {
            System.out.println(pq.poll());
        }
    }

    @SneakyThrows
    @Test
    public void test4_1() {
        // 创建优先级阻塞队列
        PriorityBlockingQueue<Integer> queue = new PriorityBlockingQueue<>();

// 添加元素 - 不会阻塞，因为队列无界
        queue.put(5);
        queue.put(1);
        queue.put(3);

// 取出元素 - 按优先级顺序
//        System.out.println(queue.take()); // 输出 1 (最小优先级)
//        System.out.println(queue.take()); // 输出 3
//        System.out.println(queue.take()); // 输出 5


        System.out.println("优先级队列内容: " + queue);
        while (!queue.isEmpty()) {
            System.out.println(queue.poll());
        }
    }

    @Test
    public void test4_2() {
        // 使用自定义比较器
        PriorityBlockingQueue<String> queue = new PriorityBlockingQueue<>(
                11, // 初始容量
                (a, b) -> b.length() - a.length() // 按字符串长度降序
        );

        queue.put("c");
        queue.put("aaa");
        queue.put("bb");

//         System.out.println(queue.take()); // "aaa" (最长)
//         System.out.println(queue.take()); // "bb"
//         System.out.println(queue.take()); // "c"

        System.out.println("优先级队列内容: " + queue);
        while (!queue.isEmpty()) {
            System.out.println(queue.poll());
        }
    }

    @Test
    public void test4_3() {
        // 使用自定义比较器
        PriorityBlockingQueue<Person> queue = new PriorityBlockingQueue<>(
                11, // 初始容量
                Comparator.comparingInt(Person::getAge).reversed() //逆序
        );

        queue.put(Person.builder().name("张三").age(10).build());
        queue.put(Person.builder().name("李四").age(11).build());
        queue.put(Person.builder().name("王五").age(10).build());

//         System.out.println(queue.take()); // "aaa" (最长)
//         System.out.println(queue.take()); // "bb"
//         System.out.println(queue.take()); // "c"

        System.out.println("优先级队列内容: " + queue);
        while (!queue.isEmpty()) {
            System.out.println(queue.poll());
        }
    }

    @Test
    public void test5() {
        /// 支持从两端操作的队列，例如 ArrayDeque。
        Deque<Integer> deque = new ArrayDeque<>();
        deque.addFirst(1);
        deque.addLast(2);

        System.out.println("双端队列内容: " + deque);
    }

}
