package com.github.axinger.service;

import org.springframework.scheduling.annotation.Async;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public interface FutureTaskService {

    void testAsync1();

    Future<String> asyncGetResult1();

    Future<String> asyncGetResult2();


    CompletableFuture<String> asyncInternalCalls();

    CompletableFuture<String> asyncInternalCalls2();

    CompletableFuture<String> actionByTime(long seconds);

    @Async
    CompletableFuture<String> actionByTime2(long seconds);

    CompletableFuture<String> asyncTes1(long seconds);

    CompletableFuture<String> asyncTes2(long seconds);

    CompletableFuture<String> asyncTes3(long seconds);

    CompletableFuture<Map> asyncTes1And2();

    Map asyncTes1And2_2() throws ExecutionException, InterruptedException;

    Object completableAll();

    CompletableFuture<String> test(boolean success);
}
