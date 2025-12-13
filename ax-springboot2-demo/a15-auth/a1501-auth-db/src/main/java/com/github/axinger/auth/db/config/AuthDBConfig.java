package com.github.axinger.auth.db.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.github.axinger.auth.db.mapper")
public class AuthDBConfig {
}
