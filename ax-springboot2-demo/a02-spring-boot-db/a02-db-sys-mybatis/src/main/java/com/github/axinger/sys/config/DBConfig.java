package com.github.axinger.sys.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.github.axinger.sys.mapper")
public class DBConfig {
}
