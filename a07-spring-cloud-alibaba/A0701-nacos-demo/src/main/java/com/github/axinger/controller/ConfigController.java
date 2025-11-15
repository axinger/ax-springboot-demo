package com.github.axinger.controller;

import com.axing.common.util.json.JsonUtil;
import com.github.axinger.bean.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName ConfigController.java
 * @Description TODO
 * @createTime 2021年12月16日 20:21:00
 */

@Slf4j
@RestController
@RefreshScope // 支持nacos的动态刷新功能
public class ConfigController {

    @Value("${axinger.doc.title:#{null}}")
    private String title;

    @Autowired
    private DocInfoProperties docInfoProperties;

    @Autowired
    private FilterProperties filterProperties;

    @Autowired
    private SysUser sysUser;

    @Autowired
    private SysUser2 sysUser2;

    @Autowired
    private SysUser3 sysUser3;

    @Autowired
    private Environment environment;

    @GetMapping("/")
    public Object getInfo() {
        Map<String, Object> map = new HashMap<>(16);
        map.put("title", title);
        map.put("person", filterProperties);
        map.put("doc", docInfoProperties);
        // class 不要单独使用 @Configuration  ,不然无整体返回给webmvc,但是可以正常取值,但可以属性一个个获取, 统一使用 EnableConfigurationProperties
//        map.put("user", user);

        try {
            map.put("user", JsonUtil.toJsonStr(sysUser));
        } catch (Exception e) {
            log.error("sysUser error: {}", e.getMessage());
        }

        try {

            System.out.println("user = " + sysUser.getName());
            System.out.println("sysUser.toString = " + sysUser);
            map.put("sysUser2", JsonUtil.toJsonStr(sysUser2));
        } catch (Exception e) {
            log.error("sysUser2 error: {}", e.getMessage());
        }

        try {
            map.put("sysUser3", JsonUtil.toJsonStr(sysUser3));

        } catch (Exception e) {
            log.error("sysUser3 error: {}", e.getMessage());
        }

        try {
            String username = environment.getProperty("axinger.user.name", String.class);
            map.put("Environment方式:axinger.user.name", username);
        } catch (Exception e) {
            log.error("Environment方式 error: {}", e.getMessage());
        }

        System.out.println("user = " + sysUser);
        System.out.println("sysUser2 = " + sysUser2);
        System.out.println("sysUser3 = " + sysUser3);
        return map;
    }
}
