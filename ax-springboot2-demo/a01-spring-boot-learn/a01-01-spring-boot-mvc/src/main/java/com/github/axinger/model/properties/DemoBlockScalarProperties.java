package com.github.axinger.model.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * YAML 块标量（Block Scalars）演示类
 * 展示 > 折叠样式、| 字面样式及其后缀变体
 */
@Data
@ConfigurationProperties(prefix = "demo.block")
public class DemoBlockScalarProperties {

    // > 折叠样式（Folded）
    private String foldedDefault;
    private String foldedStrip;
    private String foldedKeep;

    // | 字面样式（Literal）
    private String literalDefault;
    private String literalStrip;
    private String literalKeep;

    // 实际应用场景
    private String jsonConfig;
    private String sqlScript;
    private String markdownText;
    private String longDescription;

    // 集合类型中的块标量
    private List<String> multiLineList;
    private Map<String, String> multiLineMap;
}
