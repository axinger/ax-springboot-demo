package com.github.axinger.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 日志演示 Controller - 展示通用日志和业务日志的使用
 *
 * @author xing
 */
@RestController
@RequestMapping("/log")
public class LogDemoController {

    /**
     * 通用日志 - 使用类名作为 logger name
     * 日志输出到: console, info.log, debug.log, warn.log, error.log, all.log
     */
    private static final Logger log = LoggerFactory.getLogger(LogDemoController.class);

    /**
     * 订单业务日志 - 输出到独立的订单日志文件
     * 日志输出到: order_info.log, order_error.log
     */
    private static final Logger orderLog = LoggerFactory.getLogger("ORDER_LOG");

    /**
     * 支付业务日志 - 输出到独立的支付日志文件
     * 日志输出到: payment_info.log, payment_error.log
     */
    private static final Logger paymentLog = LoggerFactory.getLogger("PAYMENT_LOG");


    /**
     * 测试通用日志 - 各级别日志输出
     */
    @GetMapping("/common")
    public Map<String, Object> testCommonLog() {
        log.debug("这是 DEBUG 级别的通用日志 - 用于开发调试");
        log.info("这是 INFO 级别的通用日志 - 用于记录正常流程");
        log.warn("这是 WARN 级别的通用日志 - 用于记录警告信息");

        Map<String, Object> result = new HashMap<>();
        result.put("message", "通用日志测试完成，请查看控制台和日志文件");
        result.put("logFiles", new String[]{"info.log", "debug.log", "warn.log", "all.log"});
        return result;
    }

    /**
     * 测试订单业务日志
     */
    @PostMapping("/order/create")
    public Map<String, Object> createOrder(@RequestParam String orderNo,
                                           @RequestParam String productName,
                                           @RequestParam Double amount) {
        // 使用业务日志记录订单关键流程
        orderLog.info("订单创建开始 | orderNo={} | productName={} | amount={}", orderNo, productName, amount);

        try {

            orderLog.info("订单创建成功 | orderNo={} | status=SUCCESS", orderNo);

            Map<String, Object> result = new HashMap<>();
            result.put("orderNo", orderNo);
            result.put("status", "SUCCESS");
            result.put("message", "订单创建成功");
            return result;

        } catch (Exception e) {
            orderLog.error("订单创建失败 | orderNo={} | error={}", orderNo, e.getMessage(), e);
            throw new RuntimeException("订单创建失败: " + e.getMessage());
        }
    }

    /**
     * 测试支付业务日志
     */
    @PostMapping("/payment/pay")
    public Map<String, Object> processPayment(@RequestParam String payNo,
                                              @RequestParam String orderNo,
                                              @RequestParam Double amount) {
        // 使用业务日志记录支付关键流程
        paymentLog.info("支付处理开始 | payNo={} | orderNo={} | amount={}", payNo, orderNo, amount);

        try {

            paymentLog.info("支付处理成功 | payNo={} | orderNo={} | status=SUCCESS", payNo, orderNo);

            Map<String, Object> result = new HashMap<>();
            result.put("payNo", payNo);
            result.put("orderNo", orderNo);
            result.put("status", "SUCCESS");
            result.put("message", "支付成功");
            return result;

        } catch (Exception e) {
            paymentLog.error("支付处理失败 | payNo={} | orderNo={} | error={}", payNo, orderNo, e.getMessage(), e);
            throw new RuntimeException("支付失败: " + e.getMessage());
        }
    }

    /**
     * 测试完整业务流程 - 订单 + 支付
     */
    @PostMapping("/full-process")
    public Map<String, Object> fullProcess(@RequestParam String orderNo,
                                           @RequestParam String productName,
                                           @RequestParam Double amount) {
        // 通用日志记录整个流程
        log.info("开始处理完整业务流程 | orderNo={}", orderNo);

        // 创建订单
        orderLog.info("【业务流程】订单创建 | orderNo={} | productName={}", orderNo, productName);
        orderLog.info("【业务流程】订单创建完成 | orderNo={}", orderNo);

        // 生成支付单号
        String payNo = "PAY" + System.currentTimeMillis();

        // 处理支付
        paymentLog.info("【业务流程】支付处理 | payNo={} | orderNo={} | amount={}", payNo, orderNo, amount);
        paymentLog.info("【业务流程】支付处理完成 | payNo={} | orderNo={}", payNo, orderNo);

        log.info("完整业务流程处理完成 | orderNo={} | payNo={}", orderNo, payNo);

        Map<String, Object> result = new HashMap<>();
        result.put("orderNo", orderNo);
        result.put("payNo", payNo);
        result.put("status", "COMPLETED");
        result.put("message", "订单和支付处理完成");
        return result;
    }

    /**
     * 模拟订单异常 - 测试错误日志
     */
    @PostMapping("/order/error")
    public Map<String, Object> simulateOrderError(@RequestParam String orderNo) {
        orderLog.info("订单处理开始 | orderNo={}", orderNo);

        try {
            // 模拟异常
            if (true) {
                throw new RuntimeException("库存不足");
            }
        } catch (Exception e) {
            orderLog.error("订单处理异常 | orderNo={} | error={}", orderNo, e.getMessage(), e);
            Map<String, Object> result = new HashMap<>();
            result.put("orderNo", orderNo);
            result.put("status", "FAILED");
            result.put("error", e.getMessage());
            return result;
        }

        return new HashMap<>();
    }
}
