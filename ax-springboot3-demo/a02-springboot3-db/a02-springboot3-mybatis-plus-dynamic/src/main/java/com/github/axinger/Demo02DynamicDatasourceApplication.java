package com.github.axinger;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = {"com.github.axinger.mapper"})
public class Demo02DynamicDatasourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(Demo02DynamicDatasourceApplication.class, args);
    }

}
