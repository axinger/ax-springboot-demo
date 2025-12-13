package com.github.axinger.helloword;

import org.springframework.stereotype.Component;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName HelloService.java
 * @Description TODO
 * @createTime 2022年02月09日 14:37:00
 */

@Component
public class HelloService {

    public void sayHello(String name) {
        System.out.println("name = " + name);
    }

}
