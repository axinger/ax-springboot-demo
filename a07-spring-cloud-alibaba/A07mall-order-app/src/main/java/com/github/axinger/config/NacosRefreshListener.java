package com.github.axinger.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NacosRefreshListener {

    @Autowired
    private ApplicationContext applicationContext;

    @EventListener
    public void onRefresh(RefreshScopeRefreshedEvent event) {
        log.info("RefreshScope刷新事件: name={},source={}", event.getName(), event.getSource());
    }

    /**
     * 监听EnvironmentChangeEvent也可以
     */
    @EventListener
    public void onEnvironmentChange(org.springframework.cloud.context.environment.EnvironmentChangeEvent event) {
        log.info("EnvironmentChangeEvent: {},{}}", event.getKeys(), event.getSource());
        Environment environment = applicationContext.getEnvironment();
        for (String key : event.getKeys()) {
            String newValue = environment.getProperty(key);
            log.info("key: {}, newValue: {}", key, newValue);
        }
    }
}
