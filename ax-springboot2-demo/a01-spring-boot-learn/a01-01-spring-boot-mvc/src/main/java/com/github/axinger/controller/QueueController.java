package com.github.axinger.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

@RestController
public class QueueController {


    @Slf4j
    @RestController
    @RequestMapping("/queue1")
    public static class TestController {

        private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        private final ExecutorService executor = Executors.newSingleThreadExecutor();

        @PostConstruct
        void start() {
            executor.execute(() -> {
                while (true) {
                    try {
                        String task = queue.poll(1, TimeUnit.SECONDS);
                        if (task != null) {  // ✅ 只改了这一行
                            log.info("处理: {}", task);
                            TimeUnit.SECONDS.sleep(1);
                        }
                    } catch (Exception e) {
                        break;
                    }
                }
            });
        }

        @GetMapping("/test")
        public String test() {
            for (int i = 1; i <= 10; i++) {
                queue.offer("任务-" + i);
                log.info("提交: 任务-{}", i);
            }
            return String.format("已提交10个任务，队列大小: %d", queue.size());
        }
    }

    @Slf4j
    @RestController
    @RequestMapping("/queue2")
    public static class Test2Controller {

        private final Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();

        @PostConstruct
        void start2() {
            sink.asFlux()
                    .subscribeOn(Schedulers.newSingle("worker"))
                    .subscribe(task -> {
                        log.info("处理: {}", task);
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }

        @GetMapping("/push")
        public String push(@RequestParam String data) {
            sink.tryEmitNext(data);
            return "pushed: " + data;
        }

        @GetMapping("/test2")
        public String test2() {
            for (int i = 1; i <= 10; i++) {
                sink.tryEmitNext("任务-" + i);
            }
            return "已提交10个任务";
        }
    }
}
