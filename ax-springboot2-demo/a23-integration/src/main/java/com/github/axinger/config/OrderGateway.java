package com.github.axinger.config;

import com.github.axinger.model.Order;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.stereotype.Component;

@Component
//1. 消息网关（外部系统入口）
@MessagingGateway(defaultRequestChannel = "orderInputChannel") // 绑定到输入通道
public interface OrderGateway {
    /**
     * 接收订单消息
     *
     * @param order 订单JSON字符串
     */
    void receiveOrder(Order order);
}
