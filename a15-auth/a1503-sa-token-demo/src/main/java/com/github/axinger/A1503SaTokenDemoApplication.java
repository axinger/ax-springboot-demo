package com.github.axinger;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.dao.SaTokenDaoForRedisTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {SaTokenDaoForRedisTemplate.class})
public class A1503SaTokenDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(A1503SaTokenDemoApplication.class, args);
        System.out.println("启动成功：Sa-Token配置如下：" + SaManager.getConfig());
    }
}
