package com.github.axinger.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;

@EnableDiscoveryClient
@Configuration
// @RefreshScope // 不要放这里
public class NacosDiscoveryConfiguration {
}
