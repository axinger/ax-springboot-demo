package com.github.axinger.service.impl;

import com.github.axinger.service.FutureTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
@Slf4j
public class FutureTaskServiceImpl implements FutureTaskService {

    @Autowired
    @Qualifier("orderExecutor")
    private Executor threadPoolExecutor;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private Executor executor;

    // AsyncResult 是spring的类型
    @Async("smsExecutor")
    @Override
    public void testAsync1() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("异步 无返回");
    }

    @Async("smsExecutor")
    @Override
    public Future<String> asyncGetResult1() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("异步1 返回结果A");
        return new AsyncResult<String>("结果A");
    }

    @Async("smsExecutor")
    @Override
    public Future<String> asyncGetResult2() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("异步2 返回结果B");
        return new AsyncResult<String>("结果B");
    }

    @Override
    @Async
    public CompletableFuture<String> asyncInternalCalls() {
        FutureTaskService self = null;
        try {
            // 必须使用接口,不会报错
            self = applicationContext.getBean(FutureTaskService.class);
//            self = (FutureTaskServiceImpl) AopContext.currentProxy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (self == null) {
            return CompletableFuture.completedFuture("self null");
        }

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        for (int i = 1; i <= 3; i++) {
            self.actionByTime2(i);
        }
        stopWatch.stop();
        String s = "统计完成时长" + stopWatch.getTotalTimeSeconds();
        log.info(s);
        return CompletableFuture.completedFuture(s);
    }

    /**
     * 内部调用,成功 ,用CompletableFuture
     *
     * @return
     */
    @Override
    public CompletableFuture<String> asyncInternalCalls2() {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<CompletableFuture> futureList = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            int finalI = i;
            futureList.add(
                    CompletableFuture.runAsync(() -> {
                        actionByTime2(finalI);
                    }, executor)
            );
        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()])).join();
        stopWatch.stop();
        String result = "统计完成时长" + stopWatch.getTotalTimeSeconds();
        log.info("Thread.currentThread() = {}, result = {}", Thread.currentThread().getName(), result);
        return CompletableFuture.completedFuture(result);
    }

    @Async
    @Override
    public CompletableFuture<String> actionByTime(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final String result = "睡眠了" + seconds + "秒";
        log.info("Thread.currentThread() = {}, result = {}", Thread.currentThread().getName(), result);
        return CompletableFuture.completedFuture(result);
    }

    @Async
    @Override
    public CompletableFuture<String> actionByTime2(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final String result = "睡眠了" + seconds + "秒";
        log.info("Thread.currentThread() = {}, result = {}", Thread.currentThread().getName(), result);
        return CompletableFuture.completedFuture(result);
    }

    @Async("smsExecutor")
    @Override
    public CompletableFuture<String> asyncTes1(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("asyncTes1...........");
        return CompletableFuture.completedFuture("asyncTes1");
    }


    @Async("smsExecutor")
    @Override
    public CompletableFuture<String> asyncTes2(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("asyncTes2...........");
        return CompletableFuture.completedFuture("asyncTes2");
    }

    @Async("smsExecutor")
    @Override
    public CompletableFuture<String> asyncTes3(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("asyncTes2...........");
        return CompletableFuture.completedFuture("asyncTes3");
    }


    @Async("smsExecutor")
    @Override
    public CompletableFuture<Map> asyncTes1And2() {

//        CompletableFuture<String> all = CompletableFuture.allOf(asyncTes1(), asyncTes2());


        CompletableFuture<Map> future3 = asyncTes1(2).thenCombine(asyncTes2(3), (r1, r2) -> {
            Map map = new HashMap();
            map.put("result1", r1);
            map.put("result2", r2);
            return map;
        });
        System.out.println("asyncTes1And2 result: " + future3.join());


        return future3;

    }

    //    @Async("smsExecutor")
    @Override
    public Object completableAll() {

        Map map = new HashMap();

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("future1 = 完成");
            return "future1 finished!";
        });
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("future2 = 完成");
            return "future2 finished!";
        });
        CompletableFuture<Void> allOf = CompletableFuture.allOf(future1, future2);

        allOf.thenAccept(v -> {
            System.out.println("thenAccept = " + v);
        });

        allOf.thenRun(() -> {
            System.out.println("thenRun 完成");
        });

        allOf.whenComplete((k, v) -> {

            System.out.println("whenComplete k = " + k);
            System.out.println("whenComplete v = " + v);
            try {
                System.out.println("allOf.get() = " + allOf.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

        future1.whenComplete((v, k) -> {
            map.put("future1", v);
        });
        future2.whenComplete((v, k) -> {
            map.put("future2", v);
        });
        System.out.println("future1: " + future1.isDone() + " future2: " + future2.isDone());

        // join() 的作用：让“主线程”等待“子线程”结束之后才能继续运行
        allOf.join();

        System.out.println("map = " + map);
        return map;
    }

    @Override
    public Map asyncTes1And2_2() throws ExecutionException, InterruptedException {

        CompletableFuture<Map> future3 = asyncTes1(2).thenCombine(asyncTes2(3), (r1, r2) -> {
            Map map = new HashMap();
            map.put("result1", r1);
            map.put("result2", r2);
            return map;
        });


        System.out.println("asyncTes1And2_2: " + future3.join());
        /// 使用CompletableFuture.allOf实现异步执行同步搜集结果
//        CompletableFuture<Void> allOf = CompletableFuture.allOf(asyncTes1(), asyncTes2());


        return future3.get();
    }

    @Override
    public CompletableFuture<String> test(boolean success) {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            log.info("异步执行数据");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (success) {
                return "111";
            } else {
                throw new RuntimeException("数据错误");
            }
        }, executor).exceptionally(e -> {
            log.error("e = {}", e.getCause().getMessage());
            return null;
        });

        return future;
    }

}
