package com.github.axinger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Demo12SentinelApplication {

    public static void main(String[] args) {
        SpringApplication.run(Demo12SentinelApplication.class, args);
    }

}
