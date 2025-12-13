package com.github.axinger._08synccomplete;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 异步回调
 */
public class SyncCompleteDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {


//        /// 没有返回值
//        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
//
//
//            System.out.println(Thread.currentThread().getName() + "future1");
//        });
//        future1.get();

        test2();
    }

    static void test2() {


// whenComplete主要用于注入任务完成时的回调通知逻辑
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("compute 1");
            return 1;
        });
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("compute 2");
            return 10;
        });
        CompletableFuture<Integer> future3 = future1.thenCombine(future2, (r1, r2) -> r1 + r2);
        System.out.println("future3 result: " + future3.join());
    }
}
