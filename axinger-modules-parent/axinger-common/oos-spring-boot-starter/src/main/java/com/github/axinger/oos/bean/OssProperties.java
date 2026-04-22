package com.github.axinger.oos.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "axinger.oss")
public class OssProperties {
    /**
     * 对象存储服务的URL
     */
    private String endpoint;

    /**
     * Access key
     */
    private String accessKeyId;

    /**
     * Secret key
     */
    private String secretAccessKey;

    /**
     * 区域 Region.US_EAST_1
     */
    private String region;

    /**
     * true path-style nginx 反向代理和S3默认支持 pathStyle模式 {<a href="http://endpoint/bucketname">...</a>}
     * false supports virtual-hosted-style 阿里云等需要配置为 virtual-hosted-style 模式{<a href="http://bucketname.endpoint">...</a>}
     * 只是url的显示不一样
     */
    private Boolean pathStyleAccess = true;

    /**
     * 最大线程数，默认：100
     */
    private Integer maxConnections = 100;
}
