//package com.github.axinger.config;
//
//import org.springframework.boot.actuate.health.Health;
//import org.springframework.boot.actuate.health.HealthIndicator;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.nio.file.*;
//
//@Component
//public class LogFileHealthIndicator implements HealthIndicator {
//    @Value("${logging.file.name:}")  // 默认值为空，防止未配置时报错
//    private String logPath;
//
//    @Override
//    public Health health() {
//        if (logPath == null || logPath.isEmpty()) {
//            return Health.unknown()
//                    .withDetail("reason", "未配置 logging.file.name")
//                    .build();
//        }
//
//        Path path = Paths.get(logPath);
//
//        // 检查是否为目录（如果是目录，验证其是否存在）
//        if (Files.isDirectory(path)) {
//            try {
//                long logFileCount = Files.list(path)
//                        .filter(p -> p.toString().endsWith(".log"))
//                        .count();
//                return logFileCount > 0 ?
//                        Health.up().withDetail("logFiles", logFileCount).build() :
//                        Health.down().withDetail("error", "目录下无 .log 文件").build();
//            } catch (IOException e) {
//                return Health.down(e).build();
//            }
//        }
//        // 检查是否为单个文件
//        else {
//            return Files.exists(path) ?
//                    Health.up().withDetail("file", path.toString()).build() :
//                    Health.down().withDetail("error", "文件不存在: " + path).build();
//        }
//    }
//}
