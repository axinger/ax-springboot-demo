package com.github.axinger;

import org.springframework.util.StopWatch;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName ConcurrentHashMapTests.java
 * @description TODO
 * @createTime 2022年05月08日 23:16:00
 */

public class ConcurrentHashMapTests {

    private static final ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>(900);

    public static void main(String[] args) {

        StopWatch stopWatch = new StopWatch();

        stopWatch.start("原子操作方式====计时");
        原子操作方式();
        stopWatch.stop();
        System.out.println("stopWatch = " + stopWatch.getTotalTimeSeconds());

    }

    public static void 原子操作方式() {


        ExecutorService pool = Executors.newFixedThreadPool(8);

        for (Long i = Long.valueOf(0); i < 20; i++) {
            Long finalI = i;
            pool.execute(() -> {
                String key = String.valueOf(finalI);
                map.computeIfAbsent(key, k -> String.valueOf(finalI));
            });// 开启20个线程
        }

        pool.shutdown();// 关闭线程池

        System.out.println("map size = " + map.size());
        System.out.println("map = " + map);

    }
}
