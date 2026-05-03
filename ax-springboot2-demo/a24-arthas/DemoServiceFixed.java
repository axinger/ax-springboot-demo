package com.github.axinger.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * 示例服务类 - 用于演示 Arthas 的各种功能
 * 
 * 【已修复版本】- 修复了 calculateOrderTotal 方法的 bug
 */
@Slf4j
@Service
public class DemoServiceFixed {

    private final Random random = new Random();

    /**
     * 模拟用户查询 - 可用于演示 watch/trace 命令
     */
    public List<String> getUsers(int count) {
        log.info("查询用户列表，数量: {}", count);
        
        List<String> users = new ArrayList<>();
        IntStream.range(0, count).forEach(i -> {
            users.add("User_" + i + "_" + System.currentTimeMillis());
        });
        
        // 模拟处理延迟
        sleepRandomly();
        
        return users;
    }

    /**
     * 模拟计算操作 - 可用于演示监控方法执行
     */
    public int calculate(int a, int b) {
        log.info("执行计算: {} + {}", a, b);
        
        // 模拟复杂计算
        sleepRandomly();
        
        int result = a + b;
        log.info("计算结果: {}", result);
        
        return result;
    }

    /**
     * 模拟可能抛出异常的操作 - 可用于演示异常追踪
     */
    public String processData(String input) {
        log.info("处理数据: {}", input);
        
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("输入不能为空");
        }
        
        // 模拟随机异常
        if (random.nextInt(10) < 2) { // 20% 概率抛出异常
            throw new RuntimeException("模拟随机异常");
        }
        
        sleepRandomly();
        
        return "Processed: " + input.toUpperCase();
    }

    /**
     * 模拟耗时操作 - 可用于演示性能分析
     */
    public void slowOperation() {
        log.info("开始执行慢操作");
        
        try {
            Thread.sleep(2000 + random.nextInt(3000)); // 2-5秒随机延迟
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        log.info("慢操作完成");
    }

    /**
     * 递归方法 - 可用于演示栈追踪
     */
    public int fibonacci(int n) {
        if (n <= 1) {
            return n;
        }
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    /**
     * 【已修复】计算订单总价
     * 修复内容:
     * 1. 添加了空指针检查
     * 2. 修正了折扣计算公式
     * 3. 添加了参数验证
     */
    public double calculateOrderTotal(List<Double> prices, double discount) {
        log.info("计算订单总价，商品数量: {}, 折扣: {}", 
                prices != null ? prices.size() : 0, discount);
        
        // 修复 1: 添加空指针检查
        if (prices == null || prices.isEmpty()) {
            log.warn("商品价格列表为空，返回 0");
            return 0.0;
        }
        
        // 修复 2: 验证折扣范围（0-1之间）
        if (discount < 0 || discount > 1) {
            log.warn("折扣范围无效: {}，应在 0-1 之间", discount);
            throw new IllegalArgumentException("折扣必须在 0-1 之间");
        }
        
        double total = 0;
        for (Double price : prices) {
            if (price != null && price > 0) {
                total += price;
            }
        }
        
        // 修复 3: 正确的折扣计算 - 乘以 (1 - discount)
        total = total * (1 - discount);
        
        // 确保价格不为负数
        total = Math.max(0, total);
        
        log.info("订单总价: {}", total);
        return total;
    }

    private void sleepRandomly() {
        try {
            Thread.sleep(100 + random.nextInt(400)); // 100-500ms随机延迟
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
