package com.github.axinger;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {

    @Autowired
    private MainService accountService;

    @SneakyThrows
    @GetMapping("/1")
    public void test() {
//        accountService.test();
    }

    @GetMapping("/3")
    @Async("smsExecutor")
    public void test3() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("Thread.currentThread().getId() = {}", Thread.currentThread().getName());

    }
}
