package com.ax.dubbo.consumer.controller;

import com.ax.api.service.TestDubboService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName controller.java
 * @Description TODO
 * @createTime 2021年12月18日 21:55:00
 */

@RestController
public class TestController {

    @DubboReference(version = "1.0.0"
//            check = false,
            , interfaceClass = TestDubboService.class)
    TestDubboService testDubboService;


    @GetMapping("/test")
    public Object account() {
        System.out.println("进入了ConsumerController.............");
        final Object o = testDubboService.test1("消费者调用生产者");
        System.out.println("Dubbo调用返回值 = " + o);
        return o;
    }
}
