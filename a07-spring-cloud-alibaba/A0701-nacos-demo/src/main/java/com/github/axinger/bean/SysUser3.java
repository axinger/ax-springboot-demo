package com.github.axinger.bean;

import com.axing.common.util.json.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;

// class 不要单独使用 @Configuration  ,不然无整体返回给webmvc,但是可以正常取值,但可以属性一个个获取, 统一使用 EnableConfigurationProperties

//实时更新, 版本不一样,效果不一样

/// 正确使用方式
@Slf4j
@Data
@RefreshScope
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "axinger.user3")
@Order(13)
public class SysUser3 {
    private String name;
    private Integer age;

    private String fullName;

    @PostConstruct
    public void init() {
        try {
            log.info("\n\n👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇");
            log.info("User3初始化={}", JsonUtil.toJsonStr(this));
            log.info("\n👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆\n");
        } catch (Exception e) {
            log.error("User3初始化 error: {}", e.getMessage());
        }
        this.fullName = name + " " + age;
    }
}
