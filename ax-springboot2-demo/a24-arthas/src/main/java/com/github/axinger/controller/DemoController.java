package com.github.axinger.controller;

import com.github.axinger.model.User;
import com.github.axinger.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Arthas 演示控制器
 * 提供多个 API 接口用于演示 Arthas 的各种功能
 */
@Slf4j
@RestController
@RequestMapping("/api/demo")
public class DemoController {

    @Autowired
    private DemoService demoService;

    /**
     * 获取用户列表 - 可用于演示 watch/trace 命令
     */
    @GetMapping("/users")
    public Map<String, Object> getUsers(@RequestParam(defaultValue = "5") int count) {
        log.info("API调用: 获取用户列表, count={}", count);
        
        List<String> users = demoService.getUsers(count);
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", users);
        result.put("message", "成功");
        
        return result;
    }

    /**
     * 执行计算 - 可用于演示方法监控
     */
    @GetMapping("/calculate")
    public Map<String, Object> calculate(
            @RequestParam int a, 
            @RequestParam int b) {
        log.info("API调用: 计算 {} + {}", a, b);
        
        int result = demoService.calculate(a, b);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("data", result);
        response.put("message", "计算成功");
        
        return response;
    }

    /**
     * 处理数据 - 可用于演示异常追踪
     */
    @PostMapping("/process")
    public Map<String, Object> processData(@RequestBody Map<String, String> request) {
        String input = request.get("input");
        log.info("API调用: 处理数据, input={}", input);
        
        try {
            String result = demoService.processData(input);
            
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("data", result);
            response.put("message", "处理成功");
            
            return response;
        } catch (Exception e) {
            log.error("数据处理失败", e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "处理失败: " + e.getMessage());
            
            return errorResponse;
        }
    }

    /**
     * 慢操作 - 可用于演示性能分析
     */
    @GetMapping("/slow")
    public Map<String, Object> slowOperation() {
        log.info("API调用: 执行慢操作");
        
        long startTime = System.currentTimeMillis();
        demoService.slowOperation();
        long endTime = System.currentTimeMillis();
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("data", "操作完成，耗时: " + (endTime - startTime) + "ms");
        response.put("message", "成功");
        
        return response;
    }

    /**
     * Fibonacci 计算 - 可用于演示递归方法追踪
     */
    @GetMapping("/fibonacci")
    public Map<String, Object> fibonacci(@RequestParam(defaultValue = "10") int n) {
        log.info("API调用: 计算斐波那契数列, n={}", n);
        
        // 限制最大值以避免过长的计算时间
        if (n > 30) {
            n = 30;
        }
        
        long startTime = System.currentTimeMillis();
        int result = demoService.fibonacci(n);
        long endTime = System.currentTimeMillis();
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("data", result);
        response.put("message", "计算完成，耗时: " + (endTime - startTime) + "ms");
        
        return response;
    }

    /**
     * 获取系统信息 - 可用于演示 JVM 相关命令
     */
    @GetMapping("/system-info")
    public Map<String, Object> getSystemInfo() {
        log.info("API调用: 获取系统信息");
        
        Map<String, Object> info = new HashMap<>();
        info.put("javaVersion", System.getProperty("java.version"));
        info.put("javaVendor", System.getProperty("java.vendor"));
        info.put("osName", System.getProperty("os.name"));
        info.put("osVersion", System.getProperty("os.version"));
        info.put("availableProcessors", Runtime.getRuntime().availableProcessors());
        info.put("totalMemory", Runtime.getRuntime().totalMemory());
        info.put("freeMemory", Runtime.getRuntime().freeMemory());
        info.put("maxMemory", Runtime.getRuntime().maxMemory());
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("data", info);
        response.put("message", "成功");
        
        return response;
    }

    /**
     * 【有Bug的接口】计算订单总价 - 用于演示 Arthas 热更新修复
     * Bug: 空指针异常 + 折扣计算错误
     */
    @PostMapping("/order-total")
    public Map<String, Object> calculateOrderTotal(@RequestBody Map<String, Object> request) {
        log.info("API调用: 计算订单总价");
        
        try {
            @SuppressWarnings("unchecked")
            List<Double> prices = (List<Double>) request.get("prices");
            double discount = ((Number) request.get("discount")).doubleValue();
            
            double total = demoService.calculateOrderTotal(prices, discount);
            
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("data", total);
            response.put("message", "计算成功");
            
            return response;
        } catch (Exception e) {
            log.error("订单总价计算失败", e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "计算失败: " + e.getMessage());
            errorResponse.put("errorType", e.getClass().getSimpleName());
            
            return errorResponse;
        }
    }
}
