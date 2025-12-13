package com.github.axinger.controller;


import com.github.axinger.service.FutureTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@Slf4j
public class JUCController {

    @Autowired
    FutureTaskService taskService;
    @Autowired
    FutureTaskService futureTaskService;

    @Autowired
    private Executor executor;

    @GetMapping("/test")
    public Object test(boolean success) {
        CompletableFuture<String> future = futureTaskService.test(success);
        future.whenComplete((k, v) -> {
            System.out.println("whenComplete k = " + k + ", v = " + v);
        });
        String join = future.join();
        System.out.println("future.join() = " + join);
        return join;
    }


    @RequestMapping("/async")
    public Object async() throws InterruptedException, ExecutionException {

        // 开始时间
        long stime = System.nanoTime();

        taskService.testAsync1();
        Future<String> r1 = taskService.asyncGetResult1();
        Future<String> r2 = taskService.asyncGetResult2();

        Map map = new HashMap();
        map.put("r1", r1.get());
        map.put("r2", r2.get());

        log.info("Controller执行完毕");
        // 结束时间
        long etime = System.currentTimeMillis();

        float seconds = (etime - stime) / 1000F;
        System.out.println("seconds = " + seconds);
        log.info("执行时长：{} 秒", seconds);
        return map;
    }

    @RequestMapping("/completableFuture")
    public Object test2() throws ExecutionException, InterruptedException {

        // 开始时间
        long sTime = System.currentTimeMillis();

        CompletableFuture<String> future1 = taskService.asyncTes1(5);
        log.info("completableFuture 等待1");
        CompletableFuture<String> future2 = taskService.asyncTes2(8);
        log.info("completableFuture 等待2");

        /// 这个会超时
        CompletableFuture<String> future3 = taskService.asyncTes3(10);
        log.info("completableFuture 等待2");


        Map map = new HashMap();

//        map.put("r2", r2.get());

        future1.whenComplete((k, v) -> {
            map.put("future1", k);
        });

        future2.whenComplete((k, v) -> {
            map.put("future2", k);
        });
        future3.whenComplete((k, v) -> {
            map.put("future3", k);
        });
        CompletableFuture.allOf(future1, future2, future3).join();


        log.info("completableFuture 都有结果 = " + map);
        // 结束时间
        long eTime = System.currentTimeMillis();

        float seconds = (eTime - sTime) / 1000F;
        log.info("执行时长：{} 秒, 应该小于3秒}", seconds);
        return map;
    }

    @RequestMapping("/async3")
    public Object test3() throws ExecutionException, InterruptedException {
        return taskService.asyncTes1And2().get();
    }

    @RequestMapping("/async31")
    public Object test3_1() throws ExecutionException, InterruptedException {
        return taskService.asyncTes1And2_2();
    }

    @RequestMapping("/async4")
    public Object test4() throws ExecutionException, InterruptedException {
        return taskService.asyncTes1And2().join();
    }

    @RequestMapping("/completableAll")
    public Object completableAll() {
        final Object all = taskService.completableAll();
        System.out.println("all = " + all);
        return all;
    }

    @RequestMapping("/in1")
    public Object test9() {
        final String join = futureTaskService.asyncInternalCalls().join();
        return join;
    }

    /**
     * 内部调用,成功 ,用CompletableFuture
     *
     * @return
     */
    @RequestMapping("/in2")
    public Object test11() {
        final String join = futureTaskService.asyncInternalCalls2().join();
        return join;
    }


    @RequestMapping("/test10")
    public Object test10() {
        final String join = futureTaskService.actionByTime(2).join();
        return join;
    }


    @RequestMapping("/test12")
    public Object test12() {
        List<Integer> list = List.of(1, 2, 3, 4, 0);
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        AtomicInteger successCount = new AtomicInteger(list.size());
        for (Integer integer : list) {
            futureList.add(CompletableFuture.runAsync(() -> {
                try {
                    TimeUnit.SECONDS.sleep(integer);
                    int a = 1 / integer;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }, executor).exceptionally(e -> {
                successCount.getAndDecrement();
                return null;
            }));
        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();
        String msg = successCount.get() == list.size() ? "全部成功" : "有失败";
        return Map.of("data", msg, "count", successCount.get());
    }
}
