package com.github.axinger.controller;

import com.github.axinger.model.Order;
import com.github.axinger.service.OrderService;
import com.github.axinger.service.metrics.RequestMetrics;
import io.micrometer.core.instrument.Timer;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final RequestMetrics requestMetrics;

    public OrderController(OrderService orderService, RequestMetrics requestMetrics) {
        this.orderService = orderService;
        this.requestMetrics = requestMetrics;
    }

    @PostMapping
    public Order createOrder(@RequestParam String customerId,
                             @RequestParam double amount) throws InterruptedException {
        Timer.Sample timer = requestMetrics.startApiRequestTimer();
        try {
            Order order = orderService.createOrder(customerId, amount);
            return order;
        } finally {
            requestMetrics.stopApiRequestTimer(timer, "createOrder");
        }
    }

    @GetMapping("/metrics-demo")
    public String metricsDemo() {
        // 演示各种指标更新
        for (int i = 0; i < 10; i++) {
            try {
                orderService.createOrder("cust-" + i, 100 * (i + 1));
            } catch (Exception e) {
                // 忽略错误继续执行
            }
        }
        return "Metrics demo executed. Check /actuator/prometheus";
    }
}
