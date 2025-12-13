package com.github.axinger;

import cn.hutool.core.exceptions.ExceptionUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@SpringBootTest
@Slf4j
class CompletableFutureTest {


    @Autowired
    @Qualifier("orderExecutor")
    private Executor orderExecutor;

    @Autowired
    @Qualifier("smsExecutor")
    private Executor smsExecutor;

    @Autowired
    private Executor executor;

    @Async("cExecutor")
    public void method1(long timeout, String name) {
        try {
            TimeUnit.SECONDS.sleep(timeout);
            log.info("{}执行任务完成,Thread.currentThread() = {}", name, Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Async
    void test1() {

        List<CompletableFuture> futureList = new ArrayList<>();

        AtomicReference<String> orderNo = new AtomicReference<>("还没有订单");


        StopWatch watch = new StopWatch();
        watch.start();

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            method1(1, "订单");
            orderNo.set("订单号001");
            return "返回内容1";
        }, orderExecutor);

        futureList.add(future1);

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            method1(2, "短信");
            return "返回内容2";
        }, smsExecutor);

        futureList.add(future2);

        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
            method1(2, "通用");
            return "返回内容3";
        }, executor);

        futureList.add(future3);

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

    @SneakyThrows
    @Test
    void test11() {

        CompletableFuture<Object> objectCompletableFuture = new CompletableFuture<>();

        objectCompletableFuture.thenApply(val -> {
            return 5;
        }).exceptionally(throwable -> {
            // 返回默认值
            return -1;
        }).whenComplete((res, e) -> {
            // 在同一个线程执行
            System.out.println("A res = " + res);
            res = res * 10;
        });

        Object join = objectCompletableFuture.join();
        System.out.println("join = " + join);

    }

    @SneakyThrows
    @Test
    void test12() {

        CompletableFuture<Object> objectCompletableFuture = new CompletableFuture<>();

        objectCompletableFuture.thenApply(val -> {
            return 5;
        }).exceptionally(throwable -> {
            // 返回默认值
            return -1;
        }).whenComplete((res, e) -> {
            // 在同一个线程执行
            System.out.println("A res = " + res);
            res = res * 10;
        });

        Object join = objectCompletableFuture.join();
        System.out.println("join = " + join);

    }

    @SneakyThrows
    @Test
    void test2() {

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
                    log.info(" Thread.currentThread().getName() = {}", Thread.currentThread().getName());
//                    int a = 1 / 0;
                    return "返回内容1";
                }, executor)
                .whenComplete((val, error) -> {
                    log.info("whenComplete val = " + val);
                    if (error != null) {
                        log.info("error = {}", error.getMessage());
                    }
                }).thenApply(val -> {
                    log.info("thenApply val = " + val);
                    return val;
                }).exceptionally(e -> {
                    log.error("exceptionally会拦截错误 e={}", ExceptionUtil.getRootCauseMessage(e));
                    return null;
                });

        future1.join();
    }


    @Test
    void test_3() {

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            log.info(" Thread.currentThread().getName() = {}", Thread.currentThread().getName());
            return "返回内容1";
        });
    }

    @Test
    @Async
    void test_4() {

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            log.info(" Thread.currentThread().getName() = {}", Thread.currentThread().getName());
            return "返回内容1";
        });
    }


    @Test
    void test_5() {

        CompletableFuture.supplyAsync(() -> {
            log.info(" Thread.currentThread().getName() = {}", Thread.currentThread().getName());
            int i = 1 / 0;
            return 1;
        }, executor).exceptionally(throwable ->
        {
            System.out.println("throwable = " + throwable);
            return -1;
        }).thenApply(val -> {
            log.info("thenApply val = " + val);
            return val == -1 ? -2 : val;
        }).whenComplete((val, error) -> {
            log.info("whenComplete val = " + val);
            log.info("error = " + error);
        }).thenAcceptAsync(val -> { // 启用另一个线程池
            log.info("thenAcceptAsync = {}", val);
        }, orderExecutor);
    }


    @Test
    void test_6() {
        // 不拦截 exceptionally,whenComplete 才有error,
        // whenComplete有error,不会执行后面的 thenXXX
        CompletableFuture.supplyAsync(() -> {
            log.info(" Thread.currentThread().getName() = {}", Thread.currentThread().getName());
//            int i = 1 / 0;
            return 1;
        }, executor).whenComplete((val, error) -> {
            log.info("whenComplete val = " + val);
            log.info("error = " + error);
        }).thenAcceptAsync(val -> { // 启用另一个线程池
            log.info("thenAcceptAsync = {}", val);
        }, orderExecutor);
    }


    @Test
    void test_list_parallelStream() {

        /**
         * parallelStream默认使用了fork-join框架，其默认线程数是CPU核心数。
         *
         * 通过测试实践，发现有两种方法来修改默认的多线程数量
         */
        /**
         * 1、全局设置
         *
         * 在运行代码之前，加入如下代码：
         */
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "20");

        System.out.println("Runtime.getRuntime().availableProcessors() = " + Runtime.getRuntime().availableProcessors());
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        StopWatch watch = new StopWatch();
        watch.start();
        List<Integer> collect = list.parallelStream()
                .peek(val -> {
                    try {
                        TimeUnit.SECONDS.sleep(val);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());
                }).toList();
        watch.stop();

        System.out.println("collect = " + collect + " getTotalTimeSeconds = " + watch.getTotalTimeSeconds());

    }

    @Test
    void test_list_parallelStream2() throws ExecutionException, InterruptedException {

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        StopWatch watch = new StopWatch();
        watch.start();
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);


        List<Integer> collect = forkJoinPool.submit(() -> list.parallelStream()
                .map(val -> {
                    try {
                        TimeUnit.SECONDS.sleep(val);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());
                    return val;
                }).collect(Collectors.toList())).get();


        watch.stop();

        System.out.println("collect = " + collect + " getTotalTimeSeconds = " + watch.getTotalTimeSeconds());


    }


    @Test
    void test_list_parallelStream3() {

        List<Integer> list = Arrays.asList(1, 2, 3);

        StopWatch watch = new StopWatch();
        watch.start();


        List<Integer> collect = list.parallelStream()
                .peek(val -> {
                    try {
                        TimeUnit.SECONDS.sleep(val);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());
                }).toList();


        /// 模拟抢占线程
        // 假如某个任务运行时间较长，导致线程池资源占据，阻塞其它线程，这样就阻止了其他的并行流任务正常进行。
        list.parallelStream().forEach(val -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        watch.stop();

        System.out.println("collect = " + collect + " getTotalTimeSeconds = " + watch.getTotalTimeSeconds());


    }

    /**
     * stream指定线程池,用CompletableFuture
     */
    @Test
    void start() {

        List<Integer> list = Arrays.asList(1, 2, 3, 4);


        StopWatch watch = new StopWatch();
        watch.start();


        List<CompletableFuture<Integer>> futureList = list.stream().map(val -> CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(val);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());
            return val;
        }, executor)).toList();

        /// 这行可有可无,
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();
        List<Integer> collect = futureList.stream().map(val -> {
            System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());
            return val.join();
        }).toList();


        // 这个不行
//        List<Integer> collect = list.stream().map(val -> CompletableFuture.supplyAsync(() -> {
//            try {
//                TimeUnit.SECONDS.sleep(val);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());
//            return val;
//        }, executor).join()).collect(Collectors.toList());


        watch.stop();
        System.out.println("collect = " + collect + " getTotalTimeSeconds = " + watch.getTotalTimeSeconds());

    }

    @Test
    @SuppressWarnings({"all"})
    void test_延时() {
        log.info("延迟准备=========");
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            log.info("延迟开始=========");

        }, CompletableFuture.delayedExecutor(2, TimeUnit.SECONDS));
        log.info("其他任务=========");

        future.join();
        log.info("延迟结束=========");
    }

    @Test
    @SuppressWarnings({"all"})
    void test_延时2() {
        log.info("延迟准备=========");
        CompletableFuture<Object> future = new CompletableFuture<>();
        future.completeOnTimeout("123", 2, TimeUnit.SECONDS)
                .thenRun(() -> {
                    log.info("延迟开始thenRun=========");
                })

                .thenRunAsync(() -> {
                    log.info("延迟开始thenRunAsync=========");
                })

        ;

//        future.completeOnTimeout("123", 2, TimeUnit.SECONDS)
//                .runAsync(() -> {
//
//            log.info("延迟开始=========");
//        });
        log.info("其他任务=========");
        Object join = future.join();
        log.info("join = " + join);
        log.info("延迟结束=========");
    }
}
