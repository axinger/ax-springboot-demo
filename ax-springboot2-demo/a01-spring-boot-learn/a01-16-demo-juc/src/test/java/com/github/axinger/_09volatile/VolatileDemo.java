package com.github.axinger._09volatile;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName VolatileDemo.java
 * @Description TODO
 * @createTime 2021年12月14日 23:28:00
 */


class MyData {


    /**
     * volatile 保证可见性,不保证原子性
     */
    volatile int number = 0;

    /***
     * AtomicInteger 保证原子性的, 解决 volatile 无法保证原子性
     * 1.5
     */
    AtomicInteger atomicInteger = new AtomicInteger();

    /**
     * 1.8 api
     */
    LongAdder longAdder = new LongAdder();

    Integer number2 = 0;

    public void addTo60() {
        this.number = 60;
    }

//    public synchronized void addPlusPlus() {
//        number++;
//    }

    /**
     * 线程调度,子线程写会主线程时候,可能被挂起,操作失败
     */
    public void addPlusPlus() {
        number++;
    }

    public void addAtomicPlusPlus() {
        atomicInteger.getAndIncrement();
    }

    public void addTo602() {
        synchronized (number2) {

        }
    }
}

/***
 * @author xing
 * 1.验证  Volatile 可见性
 * 1.1 加入num==0,变量之前根本没有添加 volatile 关键字修饰
 */
public class VolatileDemo {
    public static void main(String[] args) {
//        test1();

        test2();
    }

    /**
     * 测试可见性
     */
    public static void test1() {

        MyData myData = new MyData();

        new Thread(() -> {

            System.out.println(Thread.currentThread().getName() + "==begin");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myData.addTo60();
            System.out.println(myData.number);
            System.out.println(Thread.currentThread().getName() + "==end");
        }, "A1").start();

        System.out.println(Thread.currentThread().getName() + "==主线程");
        // 主线程
        while (myData.number == 0) {

            ///没有volatile修饰,会一直卡住,说明子线程修改了值,没有通知主线程,无法保证可见性
        }

        System.out.println(Thread.currentThread().getName() + "==完成" + myData.number);
    }

    /**
     * 测试原子性
     */
    public static void test2() {


        MyData myData = new MyData();
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    myData.addPlusPlus();
                    myData.addAtomicPlusPlus();
                }
            }, String.valueOf(i)).start();
        }
        // 等待20个线程完成
//        try {
//            TimeUnit.SECONDS.sleep(5);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        // 后台有默认的2个线程,主线程,和后台线程,>2 表示还有线程未执行完
        while (Thread.activeCount() > 2) {
            Thread.yield();
        }


        System.out.println("number = " + myData.number);
        System.out.println("atomicInteger = " + myData.atomicInteger);

    }
}
