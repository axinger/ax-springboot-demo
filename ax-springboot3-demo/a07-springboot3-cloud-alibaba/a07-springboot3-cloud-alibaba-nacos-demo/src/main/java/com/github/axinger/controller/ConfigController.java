package com.github.axinger.controller;

import com.github.axinger.bean.PersonProperties;
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

    @Value("${axing.doc.title:#{null}}")
//    @NacosValue("${axing.doc.title:#{null}}")
    private String title;

//    @Autowired
//    private DocInfoProperties docInfoProperties;

    @Autowired
    private PersonProperties personProperties;

    @GetMapping("/")
    public Object getInfo() {
        Map<String, Object> map = new HashMap<>(16);
        map.put("title", title);
//        map.put("info", docInfoProperties);
        map.put("person", personProperties);
        return map;
    }
}
