package com.github.axinger._12cpu;

import org.springframework.util.StopWatch;

import java.util.concurrent.TimeUnit;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName CPUTests.java
 * @Description TODO
 * @createTime 2022年02月28日 19:16:00
 */

public class CPUTests {

    public static void main(String[] args) throws InterruptedException {


        /**
         *
         * java线程数设置和系统cpu的关系

         这里的cpu个数不是指系统的cpu总个数，也不是指cpu总核心数，而是指cpu的总逻辑处理单元即超线程的个数。

         IO密集型程序（如数据库数据交互、文件上传下载、网络数据传输等等）设置线程数为2倍的总逻辑处理单元个数。

         计算密集型程序（如数据转换，递归，复杂算法，加解密程序）设置线程数为总逻辑处理单元个数+1。

         java中总逻辑处理单元个数获取方法：Runtime.getRuntime().availableProcessors()
         一般我们说一台电脑有多少个物理cpu，共多少核，共多少线程。
         CPU总核数 = 物理CPU个数 * 每颗物理CPU的核数；
         总逻辑CPU数 = 物理CPU个数 * 每颗物理CPU的核数 * 超线程数；
         * */
        System.out.println("cpu核心线程数也就是计算资源 = " + Runtime.getRuntime().availableProcessors());


        StopWatch watch = new StopWatch();
        watch.start();
        TimeUnit.SECONDS.sleep(3);

        watch.stop();
        System.out.println("watch.getTotalTimeSeconds() = " + watch.getTotalTimeSeconds());


    }
}
