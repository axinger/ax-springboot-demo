package com.github.axinger.model.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义 Converter 演示类
 * 通过 DemoIntervalConverter 将 JSON 字符串转为 List<DemoInterval>
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "demo.convert")
public class DemoConvertProperties {

    private int timeOut;
    private int connectTimeout;

    // Spring 自动调用 DemoIntervalConverter 转换
    private List<Item> intervalConfig;

    // Map 中的 Value 同样触发转换器
    private Map<String, List<Item>> intervalConfigByTakeout = new HashMap<>();

    @PostConstruct
    public void init() {
        log.info("DemoConvertProperties 初始化: intervalConfig={}, intervalConfigByTakeout={}",
                intervalConfig, intervalConfigByTakeout);
    }


    /**
     * 区间配置对象（Demo 自定义 Converter 配套数据类）
     */
    @Data
    @AllArgsConstructor
    public static class Item {
    
        private int lowerBound;
    
        private int upperBound;
    
        private String value;
    }
}
