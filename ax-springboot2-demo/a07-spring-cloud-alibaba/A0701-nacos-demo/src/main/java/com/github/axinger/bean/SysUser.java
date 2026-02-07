package com.github.axinger.bean;

import com.axing.common.util.json.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

// class 不要单独使用 @Configuration  ,不然无整体返回给webmvc,但是可以正常取值,但可以属性一个个获取, 统一使用 EnableConfigurationProperties
// 懒加载更新
@Slf4j
@Data
@Component
@RefreshScope
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "axinger.user1")
@Order(11)
public class SysUser {
    private String name;
    private Integer age;

    private String fullName;

    /// 懒加载更新,推荐使用SysUser3
    @PostConstruct
    public void init() {
        try {
            log.info("\n\n👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇");
            log.info("User1初始化={}", JsonUtil.toJsonStr(this));
            log.info("\n👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆\n");
        } catch (Exception e) {
            log.error("User1初始化 error: {}", e.getMessage());
        }
        this.fullName = name + " " + age;
    }
}
