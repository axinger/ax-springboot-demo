package com.github.axinger.controller;

import com.github.axinger.model.bean.*;
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
    MyYmlBean myYmlBean;
    @Autowired
    private ApplicationInfo applicationInfo;
    @Autowired
    private AxingerUserProperties axingerUserProperties;
    @Autowired
    private AxingerPersonProperties axingerPersonProperties;
    @Resource
    private MyUserProperties myUserProperties;

    @GetMapping("/1")
    public Object test1() {
        return applicationInfo;
    }

    @GetMapping("/2")
    public Object test2() {
        Map<String, Object> map = new HashMap<>();
//        map.put("axingerUserProperties", axingerUserProperties.all());
        // record 可以直接返回
        map.put("axingerUserProperties", axingerUserProperties);
        // class 不要单独使用 @Configuration  ,不然无整体返回给webmvc,但是可以正常取值,但可以属性一个个获取, 统一使用 EnableConfigurationProperties
//        map.put("person", axingerPersonProperties); // 不可用
        map.put("person", axingerPersonProperties.getName()); // 可以
        map.put("userProperties", myUserProperties);
        map.put("myYmlBean", myYmlBean.getList());
        return map;
    }


    @GetMapping("/3")
    public Object test3() {
        Map<String, Object> map = new HashMap<>();
        // 不支持复杂对象转换
        String newValue = environment.getProperty("my-user", "");
        map.put("newValue", newValue);
        /// 不支持复杂对象转换：无法直接将属性转换为自定义的 POJO 类或配置属性类
        MyUserProperties property = environment.getProperty("my-user", MyUserProperties.class);
        map.put("property", property);

        map.put("myUserProperties", myUserProperties);

        /// 才可以
        String username = environment.getProperty("my-user.username", "");
        map.put("username", username);

        return map;
    }

    @GetMapping("/4")
    public Object test4() {
        Map<String, Object> map = new HashMap<>();
        map.put("myUserProperties", myUserProperties);
        return map;
    }
}
