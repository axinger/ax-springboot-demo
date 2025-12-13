package com.github.axinger.config;

import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public MeterFilter renameRegionTag() {
        return MeterFilter.renameTag("order.service", "region", "location");
    }

    @Bean
    public MeterFilter disableJvmThreadsMetrics() {
        return MeterFilter.deny(id -> {
            String metricName = id.getName();
            return metricName.startsWith("jvm.threads");
        });
    }
}
