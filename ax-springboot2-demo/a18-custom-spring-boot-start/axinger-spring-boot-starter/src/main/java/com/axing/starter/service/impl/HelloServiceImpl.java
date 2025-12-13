package com.axing.starter.service.impl;

import com.axing.starter.bean.HelloProperties;
import com.axing.starter.service.HelloService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName HelloServiceImpl.java
 * @description TODO
 * @createTime 2022年06月30日 13:36:00
 */

/**
 * 默认不要放在容器中
 */
public class HelloServiceImpl implements HelloService {

    private final HelloProperties helloProperties;

    public HelloServiceImpl(HelloProperties helloProperties) {
        this.helloProperties = helloProperties;
    }

    @Override
    public Map<String, Object> sayHello(String param) {
        Map<String, Object> map = new HashMap<>();
        map.put("param", param);
        map.put("name", helloProperties.getName());
        map.put("prefix", helloProperties.getPrefix());
        map.put("suffix", helloProperties.getSuffix());
        map.put("age", helloProperties.getAge());
        return map;
    }
}
