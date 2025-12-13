package com.github.axinger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class A12SentinelApplication {

    public static void main(String[] args) {
        SpringApplication.run(A12SentinelApplication.class, args);
    }

}
