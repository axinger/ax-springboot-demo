package com.github.axinger.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(value = {"com.github.axinger.mapper"})
public class DbConfig {
}
