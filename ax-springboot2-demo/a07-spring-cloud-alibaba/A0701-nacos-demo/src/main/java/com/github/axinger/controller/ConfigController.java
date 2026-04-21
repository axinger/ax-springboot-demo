package com.github.axinger.controller;

import com.axing.common.json.util.JsonUtil;
import com.github.axinger.bean.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.TreeMap;

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
    private SysUser4 sysUser4;
    @Autowired
    private SysUser5 sysUser5;

    @Autowired
    private Environment environment;

    @GetMapping("/")
    public Object getInfo() {
        Map<String, Object> map = new TreeMap<>();
        map.put("title", title);
        map.put("person", filterProperties);
        map.put("doc", docInfoProperties);
        // class 不要单独使用 @Configuration  ,不然无整体返回给webmvc,但是可以正常取值,但可以属性一个个获取, 统一使用 EnableConfigurationProperties
//        map.put("user", user);

        /*
        @Slf4j
            @Data
            @Component
            @RefreshScope
            @NoArgsConstructor
            @AllArgsConstructor
            @ConfigurationProperties(prefix = "axinger.user")
            @Order(11)
            public class SysUser
            这样方式,不行
         */
        try {
            map.put("sysUser1", JsonUtil.toJsonStr(sysUser)); ///不行
        } catch (Exception e) {
            log.error("sysUser error: 这样方式,不行");
            map.put("sysUser1", "sysUser error: JsonUtil.toJsonStr(sysUser) 不行"); //不可以

        }

        try {
            System.out.println("user = " + sysUser.getName()); /// 可以
            System.out.println("sysUser.toString = " + sysUser); /// 可以
        } catch (Exception e) {
            log.error("sysUser.toString 不可以");
        }

        try {
            map.put("sysUser2", JsonUtil.toJsonStr(sysUser2)); //不可以
        } catch (Exception e) {
            log.error("sysUser2 error: JsonUtil.toJsonStr(sysUser2) 不行");
            map.put("sysUser2", "sysUser2 error: JsonUtil.toJsonStr(sysUser2) 不行"); //不可以
        }

        try {
            map.put("sysUser3", sysUser3); /// 可以
        } catch (Exception e) {
            log.error("sysUser3 error: 不可以");
        }


        try {
            map.put("sysUser4", sysUser4); /// 可以
        } catch (Exception e) {
            log.error("sysUser4 error: {}", e.getMessage());
        }

        try {
            map.put("sysUser5", sysUser5); /// 可以
        } catch (Exception e) {
            log.error("sysUser5 error: 不可以");
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

    @GetMapping("/5")
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
