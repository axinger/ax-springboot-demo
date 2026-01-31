package com.github.axinger.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "elasticsearch")
public class EsProperties {

    private String schema;
    private String address;
    private int port;
    private int connectTimeout;
    private int socketTimeout;
    private int connectionRequestTimeout;
    private int maxConnectNum;
    private int maxConnectPerRoute;
    private String username;
    private String password;
}
