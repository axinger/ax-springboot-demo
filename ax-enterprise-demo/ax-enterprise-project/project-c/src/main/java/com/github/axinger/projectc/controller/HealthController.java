package com.github.axinger.projectc.controller;

import com.github.axinger.common.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * All-in-One 健康检查端点
 */
@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping
    public Result<Map<String, Object>> health() {
        Map<String, Object> info = new HashMap<>();
        info.put("service", "project-c-all-in-one");
        info.put("modules", new String[]{"project-a (user-service)", "project-b (order-service)"});
        info.put("status", "UP");
        return Result.ok(info);
    }
}
