package com.github.axinger.service;

import com.github.axinger.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

//3.5 服务激活器（处理订单）
@Slf4j
@Component
public class OrderHandler {
    /**
     * 处理VIP订单（优先通道）
     */
    @ServiceActivator(inputChannel = "vipOrderChannel")
    public void handleVipOrder(Order order) {
        log.info("【VIP订单处理】订单ID：{}，金额：{}，处理线程：{}",
                order.getId(), order.getAmount(), Thread.currentThread().getName());
        // 实际业务逻辑：调用VIP服务、优先库存扣减等
    }

    /**
     * 处理普通订单（常规通道）
     */
    @ServiceActivator(inputChannel = "normalOrderChannel", outputChannel = "batchOrderChannel")
    public Order handleNormalOrder(Order order) {
        log.info("【普通订单处理】订单ID：{}，金额：{}，处理线程：{}",
                order.getId(), order.getAmount(), Thread.currentThread().getName());
        // 实际业务逻辑：常规处理流程

        return order;
    }

    @ServiceActivator(inputChannel = "defaultOrderChannel")
    public void defaultOrderHandle(Order order) {
        log.info("【默认处理处理】订单ID：{}，金额：{}，处理线程：{}",
                order.getId(), order.getAmount(), Thread.currentThread().getName());
        // 实际业务逻辑：常规处理流程
    }
}
