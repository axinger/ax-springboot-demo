package com.github.axinger.config;

import com.github.axinger.bean.DocInfoProperties;
import com.github.axinger.bean.FilterProperties;
import com.github.axinger.bean.SysUser3;
import com.github.axinger.bean.SysUser4;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@RefreshScope // 支持nacos的动态刷新功能
@EnableConfigurationProperties({
        DocInfoProperties.class,
        FilterProperties.class,
        SysUser3.class
})
public class MyAutoConfig {


    @Bean
//    @RefreshScope 必须在bean中定义
    @ConfigurationProperties(prefix = "axinger.user4")
    public SysUser4 sysUser4() {
        return new SysUser4();
    }
}
