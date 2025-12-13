package com.axing.common.minio.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置属性
 *
 * @author luoyu
 */
@Data
@ConfigurationProperties(prefix = "axinger.minio")
public class MinioProperties {

    /**
     * minio 地址
     */
    private String url = "http://localhost:9000";

    /**
     * minio 账户
     */
    private String username;

    /**
     * minio 密码
     */
    private String password;
}
