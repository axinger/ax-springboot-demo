package com.github.axinger.service.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class OrderMetrics {
    private final Counter orderCreatedCounter;
    private final Counter orderFailedCounter;
    private final DistributionSummary orderAmountSummary;
    private final ConcurrentHashMap<String, AtomicInteger> statusGauges = new ConcurrentHashMap<>();
    private final MeterRegistry registry;

    public OrderMetrics(MeterRegistry registry) {
        this.registry = registry;

        // 初始化计数器
        this.orderCreatedCounter = Counter.builder("order.created.total")
                .description("订单创建总计数器")
                .tag("application", "order-service")
                .register(registry);

        this.orderFailedCounter = Counter.builder("order.failed.total")
                .description("订单创建化败计数器")
                .register(registry);

        // 初始化分布摘要
        this.orderAmountSummary = DistributionSummary.builder("order.amount.summary")
                .description("订单金额分布,描述")
                .baseUnit("分布单位")
                .publishPercentiles(0.5, 0.95)
                .register(registry);
    }

    public void incrementCreatedOrders() {
        orderCreatedCounter.increment();
    }

    public void incrementFailedOrders() {
        orderFailedCounter.increment();
    }

    public void recordOrderAmount(double amount) {
        orderAmountSummary.record(amount);
    }

    public void updateOrderStatusGauge(String status) {
        statusGauges.computeIfAbsent(status, s -> {
            AtomicInteger gaugeValue = new AtomicInteger(0);
            Gauge.builder("订单状态计数器", gaugeValue, AtomicInteger::get)
                    .tags("status", status)
                    .register(registry);
            return gaugeValue;
        }).incrementAndGet();
    }

    public void decrementOrderStatusGauge(String status) {
        AtomicInteger gauge = statusGauges.get(status);
        if (gauge != null) {
            gauge.decrementAndGet();
        }
    }
}
