package com.github.axinger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName AppApplication.java
 * @Description TODO
 * @createTime 2022年02月12日 22:29:00
 */
@SpringBootApplication
@EnableAsync
public class Demo6Application {
    public static void main(String[] args) {
        SpringApplication.run(Demo6Application.class, args);
    }
}
