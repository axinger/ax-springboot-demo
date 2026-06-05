package com.github.axinger.model.properties;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 自定义 Converter 演示：将 JSON 字符串转为 List<DemoInterval>
 * 通过 @ConfigurationPropertiesBinding 让 Spring Boot 在绑定配置时自动发现
 */
@Slf4j
@Component
@ConfigurationPropertiesBinding
public class DemoIntervalConverter implements Converter<String, List<DemoConvertProperties.Item>> {

    @Override
    public List<DemoConvertProperties.Item> convert(@Nullable String source) {
        if (source == null || source.trim().isEmpty()) {
            return Collections.emptyList();
        }
        try {
            Map<String, String> map = JSON.parseObject(source, new TypeReference<>() {
            });
            if (map == null || map.isEmpty()) return Collections.emptyList();

            return map.entrySet().stream()
                    .map(entry -> {
                        String[] bounds = entry.getKey().split("_");
                        if (bounds.length < 2) {
                            log.warn("Invalid interval config format: {}", entry.getKey());
                            return null;
                        }
                        return new DemoConvertProperties.Item(
                                parseBound(bounds[0]),
                                parseBound(bounds[1]),
                                entry.getValue()
                        );
                    })
                    .filter(Objects::nonNull)
                    .sorted(Comparator.comparingInt(DemoConvertProperties.Item::getLowerBound))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to parse interval config: {}", source, e);
            return Collections.emptyList();
        }
    }

    private int parseBound(String bound) {
        if (bound == null) {
            return 0;
        }
        String trimmed = bound.trim();
        return "+∞".equals(trimmed) ? Integer.MAX_VALUE : Integer.parseInt(trimmed);
    }
}
