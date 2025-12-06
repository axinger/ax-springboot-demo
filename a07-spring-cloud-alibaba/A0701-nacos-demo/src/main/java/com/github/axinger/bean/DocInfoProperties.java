package com.github.axinger.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/*

axinger:
  doc:
    title: nacos案例
    description: 123
    version: v1234
    websiteName: 11111
    websiteUrl: 11111
 */
@Slf4j
@Data
//@Component
@RefreshScope
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "axinger.doc")
//@NacosConfigurationProperties(prefix = "axinger.doc", dataId = "docInfo.yaml")
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
