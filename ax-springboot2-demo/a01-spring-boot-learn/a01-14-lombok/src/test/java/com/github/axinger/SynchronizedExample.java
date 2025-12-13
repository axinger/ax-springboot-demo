package com.github.axinger;

import lombok.Synchronized;

/*
@Synchronized

通过锁代码块的方式实现同步锁。
当synchronized修饰类属性时，通常用于定义同步代码块，
此时需要指定一个锁对象。这个锁对象通常是类的某个私有静态成员变量，
因为类属性是静态的，所以锁也应该是静态的，以确保所有访问该属性的线程都使用同一个锁。
 */
public class SynchronizedExample {
    private final Object readLock = new Object();

    @Synchronized
    public static void hello() {
        System.out.println("world");
    }

    @Synchronized
    public int answerToLife() {
        return 42;
    }

    @Synchronized("readLock")
    public void foo() {
        System.out.println("bar");
    }
}
