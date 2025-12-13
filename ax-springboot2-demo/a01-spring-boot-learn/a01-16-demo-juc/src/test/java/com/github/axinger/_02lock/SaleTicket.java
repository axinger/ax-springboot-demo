package com.github.axinger._02lock;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Ticket {

    /**
     * Lock 可以响应中断,需要放入 try finally中,保证安全
     * 默认非公平锁 (公平锁与非公平锁)
     * <p>
     * 特点
     * 非公平锁: 其他线程饿死,效率高
     * 公平锁: 雨露均沾,效率低
     */
    private final Lock lock = new ReentrantLock(true);
    private Integer num = 30;

    public void sale() {
        lock.lock();
        try {
            if (num > 0) {
                System.out.println("lock方式" + Thread.currentThread().getName() + "卖出:" + (num--) + "剩余" + num);

            }
        } finally {
            lock.unlock();
        }


    }


}


class SaleTicket {

    public static void main(String[] args) {
        Ticket ticket = new Ticket();


        new Thread(() -> {
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        }, "B1").start();


        new Thread(() -> {
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        }, "B2").start();

        new Thread(() -> {
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        }, "B3").start();


    }


}
