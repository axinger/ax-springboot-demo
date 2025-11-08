package com.github.axinger.controller;

import com.github.axinger.bean.DocInfoProperties;
import com.github.axinger.bean.FilterProperties;
import com.github.axinger.bean.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
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

    @GetMapping("/")
    public Object getInfo() {
        Map<String, Object> map = new HashMap<>(16);
        map.put("title", title);
        map.put("person", filterProperties);
        map.put("doc", docInfoProperties);
        // class 不要单独使用 @Configuration  ,不然无整体返回给webmvc,但是可以正常取值,但可以属性一个个获取, 统一使用 EnableConfigurationProperties
//        map.put("user", user);

        System.out.println("user = " + sysUser);
        return map;
    }
}
