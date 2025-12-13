package com.github.axinger.model;

// 定义订单可能处于的状态
public enum OrderState {
    // 订单已创建
    CREATED,
    // 订单已支付
    PAID,
    // 订单已发货
    SHIPPED,
    // 订单已送达
    DELIVERED,
    // 订单已取消
    CANCELLED
}
