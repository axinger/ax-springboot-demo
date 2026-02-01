package com.github.axinger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author xing
 */
@SpringBootApplication
@EnableFeignClients
public class A0601OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(A0601OrderApplication.class, args);
    }

}
