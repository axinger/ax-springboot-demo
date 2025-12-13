package com.github.axinger._02lock;

import lombok.Locked;
import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

class Phone {

    private final Object lock = new Object();

    @SneakyThrows
    public synchronized void sendSMS() {
        TimeUnit.SECONDS.sleep(4);
        System.out.println(".......sendSMS");
    }

    public synchronized void sendEmail() {
        System.out.println(".......sendEmail");
    }

    //使用 synchronized 修饰方法，等同于 synchronized(this)
    public void sendQQ() {
        synchronized (this) {
            System.out.println("synchronized(this)");
        }
    }

    @Locked // 默认使用lock字段
    public void sendWx() {
        synchronized (this) {
            System.out.println("synchronized(this)");
        }
    }


    public void getHello() {
        System.out.println(".......getHello");
    }


}


/**
 * 8种锁方法
 * 1 标准访问,先短信还是邮件
 * .......sendSMS
 * .......sendEmail
 * <p>
 * 2.停4秒在短信方法内,标准访问,短信还是邮件
 * .......sendSMS
 * .......sendEmail
 * <p>
 * 1,2 锁定当前的对象
 * <p>
 * <p>
 * 3.停4秒在短信方法内 新增普通的hello方法,短信还是hello
 * .......getHello
 * .......sendSMS
 * <p>
 * 4.停4秒在短信方法内,2个手机,短信还是邮件
 * .......sendEmail
 * .......sendSMS
 * <p>
 * 2个实例, 锁不同对象
 * <p>
 * <p>
 * 5.停4秒在短信方法内,2个静态同步方法,1个手机,短信还是邮件
 * .......sendSMS
 * .......sendEmail
 * <p>
 * 6.停4秒在短信方法内,2个静态同步方法,2个手机,短信还是邮件
 * .......sendSMS
 * .......sendEmail
 * <p>
 * 5,6 : 2个静态方法,类,锁的是类
 * <p>
 * <p>
 * 7.停4秒在短信方法内, 1个静态同步方法,1个普通同步手机,1不手机,短信还是邮件
 * .......sendEmail
 * .......sendSMS
 * <p>
 * 8.停4秒在短信方法内, 1个静态同步方法,1个普通同步手机,2不手机,短信还是邮件
 * .......sendEmail
 * .......sendSMS
 * <p>
 * 7,8 有2把锁,一个对象,一个类,
 * <p>
 * 总结: synchronized 实现同步的基础 每个对象都可以作为锁,3种形式
 * 1.普通同步方法,锁的是当前对象
 * 2.静态同步方法,锁的是类
 * 3.同步方法快,锁是synchronized括号配置的对象
 */
public class ThreadDemo4OfLock8 {

    public static void main(String[] args) throws InterruptedException {
        Phone phone1 = new Phone();
        Phone phone2 = new Phone();

        new Thread(() -> {
            try {
                phone1.sendSMS();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "A1").start();

        Thread.sleep(100);

        new Thread(() -> {
            try {
//                phone1.sendEmail();
//                phone1.getHello();

                phone2.sendEmail();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "A2").start();
    }
}
