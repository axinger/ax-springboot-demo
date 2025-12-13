package com.github.axinger.service.impl;

import com.github.axinger.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName OrderServiceImpl.java
 * @description TODO
 * @createTime 2022年06月08日 10:16:00
 */

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {


    @Autowired
    @Qualifier("orderExecutor")
    private Executor orderExecutor;

    @Autowired
    @Qualifier("smsExecutor")
    private Executor smsExecutor;

    /**
     * @Primary
     * @Bean 这2个参数唯一标识
     */
    @Autowired
    private Executor executor;


    public void method1(long timeout, String name) {
        try {
            TimeUnit.SECONDS.sleep(timeout);
            log.info("{}执行任务完成,Thread.currentThread() = {}", name, Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Async
    public void method2(long timeout, String name) {
        try {
            TimeUnit.SECONDS.sleep(timeout);
            log.info("{}method2,标注@Async, 执行任务完成,Thread.currentThread() = {}", name, Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void test0() {

    }

    // 没有 @Async 走主线程
//    @Async("orderExecutor")
//    @Async // 需要重写 AsyncConfigurer
    @Override
    public void test1() {
        log.info("test1========");
        List<CompletableFuture> futureList = new ArrayList<>();

        AtomicReference<String> orderNo = new AtomicReference<>("还没有订单");
        StopWatch watch = new StopWatch();
        watch.start();

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            method1(1, "订单Executor");
            orderNo.set("订单号001");
            return "返回内容1";
        }, orderExecutor);

        futureList.add(future1);

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            method1(2, "短信Executor");
            return "返回内容2";
        }, smsExecutor);

        futureList.add(future2);

        /// 不传参 Executor executor 默认系统的 ForkJoinPool.commonPool-worker 不能修改
        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
            method2(2, "不传参Executor");
            return "返回内容3";
        });

        futureList.add(future3);

        CompletableFuture<String> future4 = CompletableFuture.supplyAsync(() -> {
            method2(2, "传参,默认的Executor");
            return "返回内容4";
        }, executor);

        futureList.add(future4);

        future1.whenComplete((val, error) -> {
            if (error != null) {
                System.out.println("error = " + error);
            } else {
                System.out.println("val = " + val);
            }
        });

        log.info("主线程任务不阻塞 ======= {}", Thread.currentThread().getName());
        final CompletableFuture[] array = futureList.toArray(new CompletableFuture[futureList.size()]);

        final CompletableFuture<Void> all = CompletableFuture.allOf(array);

        CompletableFuture.anyOf(array).whenComplete((val, error) -> {
            if (error != null) {
                System.out.println("anyOf error = " + error);
            } else {
                System.out.println("anyOf val = " + val);
            }
        });


        all.whenComplete((val, error) -> {
            if (error != null) {
                System.out.println("all error = " + error);
            } else {
                System.out.println("all val = " + val);
            }
        });


        System.out.println("all.join前,订单值 = " + orderNo.get());
        all.join();
        System.out.println("all.join后,订单值 = " + orderNo.get());

        log.info("future1.join() = " + future1.join());
        log.info("future2.join() = " + future2.join());

        watch.stop();
        log.info("getTotalTimeSeconds = " + watch.getTotalTimeSeconds());

//        future1.complete("aaa");
//
//        System.out.println("future1.join() = " + future1.join());

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
