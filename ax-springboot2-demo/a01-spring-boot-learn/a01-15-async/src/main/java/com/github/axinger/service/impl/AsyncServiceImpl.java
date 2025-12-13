package com.github.axinger.service.impl;

import com.github.axinger.service.AsyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName AsyncServiceImpl.java
 * @description TODO
 * @createTime 2022年06月08日 10:20:00
 */
@Service
@Slf4j
public class AsyncServiceImpl implements AsyncService {


    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    private Executor executor;


//    @Autowired
//    AsyncService asyncService;

    /**
     * 最简单的异步调用，返回值为void
     */
    @Async
    @Override
    public void test1() {
        System.out.println("test1 currentThread = " + Thread.currentThread().getName());
    }

    /**
     * 最简单的异步调用，返回值为void
     */
    @Async
    @Override
    public void test2() {
        System.out.println("test1 currentThread = " + Thread.currentThread().getName());
    }

    /**
     * 嵌套
     */
    @Async
    @Override
    public void test3() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        System.out.println("test3 currentThread = " + Thread.currentThread().getName());
//        test4();
//        this.test4();;
//        System.out.println("((AsyncService)AopContext.currentProxy()) = " + (AopContext.currentProxy()));


        applicationContext.getBean(AsyncService.class).test4_1();
        /**
         * 和事务效果一样,使用了aop代理,所以内部调用
         * */
        applicationContext.getBean(AsyncService.class).test4();

        stopWatch.stop();
        String s = "统计完成时长" + stopWatch.getTotalTimeSeconds();
        log.info(s);

    }

    @Async
    @Override
    public void test4() {
        long seconds = 3;
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final String result = "睡眠了" + seconds + "秒";
        log.info("Thread.currentThread() = {}, result = {}", Thread.currentThread().getName(), result);

    }

    @Async
    @Override
    public void test4_1() {
        long seconds = 2;
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final String result = "睡眠了" + seconds + "秒";
        log.info("Thread.currentThread() = {}, result = {}", Thread.currentThread().getName(), result);

    }

    //    @Async("customizeThreadPool")
    @Async
    @Override
    public void test11() {
        log.info("test11");
        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
//            method2(2, "通用");
            log.info("传参 executor");
            return "返回内容3";
        }, executor);

        CompletableFuture<String> future4 = CompletableFuture.supplyAsync(() -> {
//            method2(2, "通用");
            log.info("不传参 executor");
            return "返回内容3";
        });

        int sleepSeconds = new Random().nextInt(3) + 1;
        try {
            TimeUnit.SECONDS.sleep(sleepSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
