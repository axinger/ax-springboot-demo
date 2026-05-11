package com.axinger.order.application;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 订单领域事件处理器
 * 处理订单相关的领域事件，实现业务解耦
 */
@Component
public class OrderEventHandler {

    /**
     * 处理订单创建事件
     * 在事务提交后异步执行
     */
    @EventListener
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handleOrderCreatedEvent(OrderService.OrderCreatedEvent event) {
        // 订单创建事件的简单处理
        System.out.println("处理订单创建事件: " + event.orderId() + ", 金额: " + event.amount());
    }

    /**
     * 处理订单确认事件
     * 在事务提交后异步执行
     */
    @EventListener
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handleOrderConfirmedEvent(OrderService.OrderConfirmedEvent event) {
        // 订单确认事件的简单处理
        System.out.println("处理订单确认事件: " + event.orderId());
    }

    /**
     * 处理订单取消事件
     * 在事务提交后异步执行
     */
    @EventListener
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handleOrderCancelledEvent(OrderService.OrderCancelledEvent event) {
        // 订单取消事件的简单处理
        System.out.println("处理订单取消事件: " + event.orderId());
    }
}