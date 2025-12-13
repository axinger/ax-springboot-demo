package com.github.axinger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author xing
 */
@SpringBootApplication
@EnableDiscoveryClient
public class A0708MallPaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(A0708MallPaymentApplication.class, args);
    }

}
