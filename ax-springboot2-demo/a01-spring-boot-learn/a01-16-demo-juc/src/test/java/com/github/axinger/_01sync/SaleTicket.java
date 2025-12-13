package com.github.axinger._01sync;


class Ticket {

    private Integer num = 30;

    /**
     * synchronized 关键字 加锁
     */
    public synchronized void sale() {
        if (num > 0) {
            System.out.println(Thread.currentThread().getName() + "卖出:" + (num--) + "剩余" + num);

        }
    }


}


class SaleTicket {

    public static void main(String[] args) {
        Ticket ticket = new Ticket();


        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 40; i++) {
                    ticket.sale();
                }
            }
        }, "A1").start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 40; i++) {
                    ticket.sale();
                }
            }
        }, "A2").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 40; i++) {
                    ticket.sale();
                }
            }
        }, "A3").start();


    }


}
