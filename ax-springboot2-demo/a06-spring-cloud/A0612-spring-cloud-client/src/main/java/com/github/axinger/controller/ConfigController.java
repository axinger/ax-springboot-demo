package com.github.axinger.controller;

import com.github.axinger.bean.SysUser5;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.TreeMap;


@Slf4j
@RestController
@RefreshScope // 支持nacos的动态刷新功能
public class ConfigController {

    @Autowired
    private SysUser5 sysUser5;

    @Autowired
    private Environment environment;


    @GetMapping("/")
    public Object test5() {
        Map<String, Object> map = new TreeMap<>();

        try {
            map.put("sysUser5", sysUser5); /// 可以
        } catch (Exception e) {
            log.error("sysUser5 error: 不可以");
        }
        return map;
    }
}
