package com.github.axinger.controller;

import lombok.Locked;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/lock")
public class LockController {

    int lockCount = 0;

    @Locked
    @SneakyThrows
    @GetMapping("/1")
    public String test1() {
        System.out.println("开始test1*************************************");
        lockCount += 1;
        System.out.println("lockCount1 = " + lockCount);
        TimeUnit.SECONDS.sleep(5);
        System.out.println("结束test1************************************");
        return "lockCount1:" + lockCount;
    }

    @Locked
    @SneakyThrows
    @GetMapping("/2")
    public String test2() {
        System.out.println("开始test2************************************");
        lockCount += 1;
        System.out.println("lockCount = " + lockCount);
        TimeUnit.SECONDS.sleep(2);
        System.out.println("结束test2************************************");
        return "lockCount2:" + lockCount;
    }
}
