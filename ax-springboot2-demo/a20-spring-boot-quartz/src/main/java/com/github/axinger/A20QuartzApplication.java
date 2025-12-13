package com.github.axinger;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
//@SpringBootApplication
@MapperScan(basePackages = {"com.github.axinger.mapper"})
public class A20QuartzApplication {

    public static void main(String[] args) {
        SpringApplication.run(A20QuartzApplication.class, args);
    }

}
