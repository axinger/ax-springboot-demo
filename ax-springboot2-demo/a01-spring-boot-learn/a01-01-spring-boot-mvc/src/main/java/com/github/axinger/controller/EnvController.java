package com.github.axinger.controller;

import com.github.axinger.model.properties.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/env")
@RequiredArgsConstructor
public class EnvController {
    private final Environment environment;
    @Resource
    DemoPropertySourceProperties demoPropertySourceProperties;
    @Autowired
    private DemoProperties demoProperties;
    @Autowired
    private DemoUserProperties demoUserProperties;
    @Resource
    private DemoConfigImportProperties demoConfigImportProperties;

    @GetMapping("/1")
    public Object test1() {
        return demoProperties;
    }

    @GetMapping("/2")
    public Object test2() {
        Map<String, Object> map = new HashMap<>();
        map.put("demoUserProperties", demoUserProperties);
        map.put("demoProperties", demoProperties.getPersonName());
        map.put("demoConfigImportProperties", demoConfigImportProperties);
        map.put("demoPropertySourceProperties", demoPropertySourceProperties.getList());
        return map;
    }

    @GetMapping("/3")
    public Object test3() {
        Map<String, Object> map = new HashMap<>();
        String newValue = environment.getProperty("demo.config", "");
        map.put("newValue", newValue);
        DemoConfigImportProperties property = environment.getProperty("demo.config", DemoConfigImportProperties.class);
        map.put("property", property);
        map.put("demoConfigImportProperties", demoConfigImportProperties);
        String username = environment.getProperty("demo.config.username", "");
        map.put("username", username);
        return map;
    }

    @GetMapping("/4")
    public Object test4() {
        Map<String, Object> map = new HashMap<>();
        map.put("demoConfigImportProperties", demoConfigImportProperties);
        return map;
    }
}
