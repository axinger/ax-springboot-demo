package com.github.axinger.service;


import com.github.axinger.model.Order;
import com.github.axinger.service.metrics.OrderMetrics;
import com.github.axinger.service.metrics.RequestMetrics;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class OrderService {
    private final OrderMetrics orderMetrics;
    private final RequestMetrics requestMetrics;
    private final Random random = new Random();

    public OrderService(OrderMetrics orderMetrics, RequestMetrics requestMetrics) {
        this.orderMetrics = orderMetrics;
        this.requestMetrics = requestMetrics;
    }

    public Order createOrder(String customerId, double amount) throws InterruptedException {
        // 模拟处理时间
        long startTime = System.nanoTime();

        try {
            // 模拟业务处理
            Thread.sleep(random.nextInt(200));

            // 10%概率失败
            if (random.nextDouble() < 0.1) {
                throw new RuntimeException("Payment failed");
            }

            Order order = new Order(
                    "ORD-" + System.currentTimeMillis(),
                    customerId,
                    amount,
                    "COMPLETED"
            );

            // 记录指标
            orderMetrics.incrementCreatedOrders();
            orderMetrics.recordOrderAmount(amount);
            orderMetrics.updateOrderStatusGauge("COMPLETED");

            return order;
        } catch (Exception e) {
            orderMetrics.incrementFailedOrders();
            orderMetrics.updateOrderStatusGauge("FAILED");
            throw e;
        } finally {
            long duration = System.nanoTime() - startTime;
            requestMetrics.recordDbQueryTime(duration, TimeUnit.NANOSECONDS);
        }
    }
}
