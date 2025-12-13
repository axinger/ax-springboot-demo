package com.axing.starter.service;

import java.util.Map;

/**
 * 默认不要放在容器中
 */
public interface HelloService {

    Map<String, Object> sayHello(String param);
}
