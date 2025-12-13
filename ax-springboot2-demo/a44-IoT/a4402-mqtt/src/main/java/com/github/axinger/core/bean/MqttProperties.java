package com.github.axinger.core.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "axinger.mqtt-server")
public class MqttProperties {


    /**
     * mqtt 地址
     */
    private String url;

    /**
     * mqtt 用户名
     */
    private String username;

    /**
     * mqtt 密码
     */
    private String password;

    /**
     * mqtt 链接的客户端id
     */
    private String clientId;

    /**
     * 订阅主题
     */
    private List<String> consumerTopic;

    /**
     * 超时链接时间
     */
    private int completionTimeout = 10;

}
