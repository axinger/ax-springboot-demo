package com.github.axinger.helloword;

import org.springframework.stereotype.Component;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName DDModel.java
 * @Description TODO
 * @createTime 2022年02月09日 14:36:00
 */

@Component
public class HelloWord {


    private final HelloService helloService;

    //
//    // setter方式注入Bean
//    public void setHelloService(HelloService helloService) {
//        System.out.println("setter注入");
//        this.helloService = helloService;
//    }
//
//    // 构造方法注入
    public HelloWord(HelloService helloService) {
        System.out.println("构造器注入");
        this.helloService = helloService;
    }

    public void doSome() {
        System.out.println("doSome.......");
        // 向大家打招呼
//        helloService.sayHello("大家好!");
    }


}

