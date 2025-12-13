package com.github.axinger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author xing
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class A0707MallOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(A0707MallOrderApplication.class, args);
    }

}
