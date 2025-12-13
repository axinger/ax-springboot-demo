package com.github.axinger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.github.axinger.model"})// 扫描包
@SpringBootApplication
public class DemoMapstructApp {

    public static void main(String[] args) {
        SpringApplication.run(DemoMapstructApp.class, args);
    }
}
