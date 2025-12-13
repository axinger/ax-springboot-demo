package com.github.axinger.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/*

axing:
  doc:
    title: nacos案例
    description: 123
    version: v1234
    websiteName: 11111
    websiteUrl: 11111
 */
@Data
//@Component
@ConfigurationProperties(prefix = "axing.doc")
//@NacosConfigurationProperties(prefix = "axing.doc", dataId = "docInfo.yaml")
@RefreshScope // 支持nacos的动态刷新功能
public class DocInfoProperties {

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 版本
     */
    private String version;

    /**
     * 网站描述
     */
    private String websiteName;

    /**
     * 网站url
     */
    private String websiteUrl;

}
