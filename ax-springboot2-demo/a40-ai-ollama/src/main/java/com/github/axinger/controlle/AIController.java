package com.github.axinger.controlle;

import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import javax.annotation.Resource;

@RestController
public class AIController {
    @Resource
    private OllamaChatClient ollamaChatClient;

    @GetMapping("/chat")
    public String chat(@RequestParam(name = "message") String message) {
        return ollamaChatClient.call(message);
    }

    @GetMapping(value = "/chat2")
    public Flux<String> chat2(@RequestParam(name = "message") String message) {
        Flux<String> flux = ollamaChatClient.stream(message);


        flux.doOnNext(System.out::println)
                .subscribeOn(Schedulers.boundedElastic()) // 使用不同调度器
                .subscribe();
        return flux;
    }

}
