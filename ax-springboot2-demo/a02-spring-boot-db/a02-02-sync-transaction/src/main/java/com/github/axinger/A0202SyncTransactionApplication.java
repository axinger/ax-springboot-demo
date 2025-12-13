package com.github.axinger;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Async
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.github.axinger.mapper")
public class A0202SyncTransactionApplication {

    public static void main(String[] args) {
        SpringApplication.run(A0202SyncTransactionApplication.class, args);
    }
}
