package com.github.axinger.config;

import com.github.axinger.bean.SysUser5;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
//@RefreshScope // 支持nacos的动态刷新功能
@EnableConfigurationProperties({
        SysUser5.class
})
public class MyAutoConfig {
}
