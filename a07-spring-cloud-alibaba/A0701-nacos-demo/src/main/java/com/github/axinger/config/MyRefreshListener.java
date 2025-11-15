package com.github.axinger.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MyRefreshListener {

    private final Environment environment;

//    @EventListener
//    public void onRefresh(RefreshScopeRefreshedEvent event) {
//        log.info("RefreshScopeRefreshedEvent刷新事件: {}", event.getName());
//
//        String[] importantKeys = {"axinger.user.age"};
//        for (String key : importantKeys) {
//            String value = environment.getProperty(key);
//            log.error("监听值变化方式1:key={},value={}", key, value);
//        }
//
//    }

    /**
     * 监听EnvironmentChangeEvent也可以
     */
    @EventListener
    public void onEnvironmentChange(org.springframework.cloud.context.environment.EnvironmentChangeEvent event) {
        log.info("\n\n👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇");
        log.info("EnvironmentChangeEvent刷新事件: {},{}}", event.getKeys(), event.getSource());
        log.info("检测到配置变更，变更的 keys: {}", event.getKeys());
        for (String key : event.getKeys()) {
            String newValue = environment.getProperty(key);
            log.info("监听值变化方式2:key={},value={}", key, newValue);
        }
        log.info("\n👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆\n");
    }
}
