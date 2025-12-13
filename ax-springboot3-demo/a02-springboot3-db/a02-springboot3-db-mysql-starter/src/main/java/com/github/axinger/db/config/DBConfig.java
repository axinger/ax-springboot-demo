package com.github.axinger.db.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.github.axinger.db.mapper")
public class DBConfig {
}
