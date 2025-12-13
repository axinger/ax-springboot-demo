package com.github.axinger.model;

// 定义能够触发订单状态转换的事件
public enum OrderEvent {
    // 支付订单事件
    PAY,
    // 发货订单事件
    SHIP,
    // 送达订单事件
    DELIVER,
    // 取消订单事件
    CANCEL
}
