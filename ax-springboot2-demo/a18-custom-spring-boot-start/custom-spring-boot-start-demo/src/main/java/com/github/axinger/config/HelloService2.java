package com.github.axinger.config;

import com.axing.starter.service.HelloService;

import java.util.Map;

public class HelloService2 implements HelloService {
    @Override
    public Map<String, Object> sayHello(String param) {
        return Map.of("param", param);
    }
}
