package com.github.axinger.bean;

import com.axing.common.util.json.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import javax.annotation.PostConstruct;

@Slf4j
@Data
@RefreshScope//实时更新
@NoArgsConstructor
@AllArgsConstructor
public class SysUser4 {
    private String name;
    private Integer age;

    private String fullName;

    @PostConstruct
    public void init() {
        try {
            log.info("\n\n👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇");
            log.info("User4初始化={}", JsonUtil.toJsonStr(this));
            log.info("\n👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆\n");
        } catch (Exception e) {
            log.error("User4初始化 error: {}", e.getMessage());
        }
        this.fullName = name + " " + age;
    }
}
