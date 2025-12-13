package com.github.axinger;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.MinMaxPriorityQueue;
import org.junit.jupiter.api.Test;

public class 队列Tests {

    @Test
    public void test1() {


// 创建最大容量为10的队列
        EvictingQueue<String> queue = EvictingQueue.create(10);

// 当队列满时，自动移除最旧的元素
        queue.add("item1");

        System.out.println("queue = " + queue);

    }

    @Test
    public void test2() {
// 创建最小最大优先级队列
        MinMaxPriorityQueue<Integer> queue = MinMaxPriorityQueue
                .maximumSize(10) // 可选限制大小
                .create();

        queue.add(5);
        queue.add(3);
        queue.add(7);
        queue.add(11);

        Integer min = queue.peekFirst(); // 3
        Integer max = queue.peekLast();  // 7
        System.out.println("min = " + min);
        System.out.println("max = " + max);
    }
}
