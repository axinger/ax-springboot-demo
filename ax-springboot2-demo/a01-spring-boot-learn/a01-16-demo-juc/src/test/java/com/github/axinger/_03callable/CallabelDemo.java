package com.github.axinger._03callable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 创建多线程方式
 * 1. 继承 Thread
 * <p>
 * 2. 实现Runable接口
 * <p>
 * 3. 实现 callable 接口,有返回值
 * <p>
 * 4. 线程池方式
 */

class MyCallable implements Callable {

    @Override
    public Integer call() throws Exception {
        return 200;
    }
}

class MyRunnable implements Runnable {

    @Override
    public void run() {

    }
}

public class CallabelDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        /**
         * 是否有返回值
         * 是否抛出异常
         * 方法名不同
         */


//        FutureTask task = new FutureTask<String>(new MyCallable());

        FutureTask<Integer> task = new FutureTask<>(() -> {
            System.out.println(Thread.currentThread().getName() + ": 200");
            return 200;
        });


        new Thread(task, "A1").start();

        while (!task.isDone()) {
            System.out.println("task_wait");
        }

        System.out.println(task.get());
        System.out.println(task.get());
        System.out.println("task_结束");


    }

}
