package com.github.axinger.seata.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName MyBatisConfig.java
 * @Description TODO
 * @createTime 2021年12月19日 01:57:00
 */
@Configuration
@MapperScan("com.ax.seata.mapper")
public class MyBatisConfig {
}
