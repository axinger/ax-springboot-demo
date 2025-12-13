package com.github.axinger.service;

import com.github.axinger.model.Order;
import org.springframework.integration.annotation.Router;
import org.springframework.stereotype.Component;

//3.4 动态路由器（按类型路由）
@Component
public class OrderRouter {
    /**
     * 根据订单类型路由到不同通道
     */
    @Router(inputChannel = "transformedOrderChannel")
    public String routeByOrderType(Order order) {
        return switch (order.getType()) {
            case "VIP" -> "vipOrderChannel"; // VIP订单通道
            case "NORMAL" -> "normalOrderChannel"; // 普通订单通道
            default -> "defaultOrderChannel"; // 默认通道
        };
    }
}
