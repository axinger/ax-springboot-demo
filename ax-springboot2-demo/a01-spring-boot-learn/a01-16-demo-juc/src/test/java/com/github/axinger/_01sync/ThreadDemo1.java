package com.github.axinger._01sync;

class Share {
    private Integer num = 0;

    //+1
    synchronized void incr() throws InterruptedException {
        // 防止虚假唤醒,用while解决
//        if (num != 0) {
//            wait();//在哪里睡,就在哪里醒
//        }
        while (num != 0) {
            wait();// 在哪里睡,就在哪里醒
        }
        num++;
        System.out.println("num = " + num + "::" + Thread.currentThread().getName());

        // 通知其他线程
        notifyAll();
    }

    // -1
    synchronized void decr() throws InterruptedException {

        while (num != 1) {
            wait();
        }
        num--;
        System.out.println("num = " + num + "::" + Thread.currentThread().getName());

        // 通知其他线程
        notifyAll();
    }

}

public class ThreadDemo1 {

    /**
     * 线程间通信,通知
     */
    public static void main(String[] args) {
        Share share = new Share();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    share.incr();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "incr_1").start();


        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    share.decr();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "decr_1").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    share.incr();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "incr_2").start();


        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    share.decr();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "decr_2").start();

    }

}
