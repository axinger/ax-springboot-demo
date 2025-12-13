package com.github.axinger.service;

import com.github.axinger.model.Order;
import org.springframework.integration.annotation.Transformer;
import org.springframework.stereotype.Component;

import java.util.Date;

//3.3 转换器（自定义字段转换）
@Component
public class OrderTransformer {
    /**
     * 补充订单创建时间（示例：自定义转换逻辑）
     */
    @Transformer(inputChannel = "validOrderChannel", outputChannel = "transformedOrderChannel")
    public Order enrichOrder(Order order) {
        order.setCreateTime(new Date()); // 补充创建时间
        return order;
    }
}
