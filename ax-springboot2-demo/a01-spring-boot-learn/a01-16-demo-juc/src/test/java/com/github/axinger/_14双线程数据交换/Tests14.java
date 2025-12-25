package com.github.axinger._14双线程数据交换;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

public class Tests14 {
//    Exchanger是Java并发库中用于两个线程间双向数据交换的同步工具类。
//    它通过提供"同步点"(Rendezvous Point)机制，使两个线程在特定点上交换各自持有的对象，实现线程间的安全协作

    @Test
    public void test() throws InterruptedException {

        Exchanger<String> stageExchanger = new Exchanger<>();

        Thread stage1 = new Thread(() -> {
            try {
                String data = "原始数据";
                System.out.println("阶段1处理数据: " + data);
                String processed = data + " (阶段1处理)";
                System.out.println("阶段1完成处理，等待交换");
                String stage2Data = stageExchanger.exchange(processed);
                System.out.println("阶段1收到阶段2结果: " + stage2Data);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread stage2 = new Thread(() -> {
            try {
                System.out.println("阶段2等待阶段1数据");
                String stage1Data = stageExchanger.exchange("阶段2准备");
                System.out.println("阶段2收到阶段1数据: " + stage1Data);
                String processed = stage1Data + " (阶段2处理)";
                System.out.println("阶段2完成处理，等待交换");
                stageExchanger.exchange(processed);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        stage1.start();
        stage2.start();

        TimeUnit.SECONDS.sleep(5);
    }
}
