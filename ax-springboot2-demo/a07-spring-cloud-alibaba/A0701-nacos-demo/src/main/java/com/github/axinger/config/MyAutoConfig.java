package com.github.axinger.config;

import com.github.axinger.bean.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@RefreshScope // 支持nacos的动态刷新功能
@EnableConfigurationProperties({
        DocInfoProperties.class,
        FilterProperties.class,
        SysUser3.class,
        SysUser5.class
})
public class MyAutoConfig {


    @Bean
//    @RefreshScope 必须在bean中定义
    @ConfigurationProperties(prefix = "axinger.user4")
    public SysUser4 sysUser4() {
        return new SysUser4();
    }
}
