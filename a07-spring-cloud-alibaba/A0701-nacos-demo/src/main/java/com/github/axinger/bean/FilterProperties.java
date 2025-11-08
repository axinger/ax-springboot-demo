package com.github.axinger.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
@RefreshScope
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "axinger.filter")
public class FilterProperties {
    private List<String> blacklist = new ArrayList<>();
    private List<String> whitelist = new ArrayList<>();
}
