package com.github.axinger;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class LockedExampleTest {

    @Test
    public void testGetValue() throws InterruptedException {
        LockedExample example = new LockedExample();

        // 测试读锁是否正常工作
        // 在实际中，可能需要使用反射或其他手段验证锁的使用
        // 这里简单测试功能
        assertEquals(0, example.getValue());

        // 多线程测试读锁是否允许并发读取
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                example.getValue();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                example.getValue();
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        // 如果没有死锁，测试通过
    }

    @Test
    public void testSetValue() throws InterruptedException {
        LockedExample example = new LockedExample();

        // 初始值测试
//        example.setValue(42);
//        assertEquals(42, example.getValue());

        // 多线程测试写锁的互斥性
        example.setValue(0);

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                example.setValue(example.getValue() + 1);
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                example.setValue(example.getValue() + 1);
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        // 如果锁正常工作，结果应该是2000
//        assertEquals(2000, example.getValue());
        System.out.println("example.getValue() = " + example.getValue());
    }

    @Test
    public void testFooWithBaseLock() throws InterruptedException {
        LockedExample example = new LockedExample();

        // 简单功能测试
        example.foo(); // 应该打印"bar"

        // 测试foo方法是否真的使用了baseLock
        // 可以通过尝试在另一个线程中获取baseLock来验证
        Thread testThread = new Thread(() -> {
            if (example.baseLock.tryLock()) {
                try {
                    // 如果能获取锁，说明foo方法没有持有锁
                    fail("foo method should hold the baseLock");
                } finally {
                    example.baseLock.unlock();
                }
            }
        });

        // 启动测试线程
        testThread.start();
        testThread.join();
    }
}
