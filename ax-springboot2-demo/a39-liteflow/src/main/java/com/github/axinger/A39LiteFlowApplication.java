package com.github.axinger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//把你定义的组件扫入Spring上下文中
@ComponentScan({"com.github.axinger.cmp"})
public class A39LiteFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(A39LiteFlowApplication.class, args);
    }
}
