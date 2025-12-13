package com.github.axinger.service;

import com.github.axinger.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.Filter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

//3.2 过滤器（过滤无效订单）
@Slf4j
@Component
public class OrderFilter {
    /**
     * 过滤金额≤0的订单
     *
     * @param order 订单对象
     * @return true：保留订单，false：丢弃订单
     */
    @Filter(inputChannel = "orderInputChannel", outputChannel = "validOrderChannel")
    public boolean filterInvalidOrder(Order order) {
        boolean isValid = order.getAmount().compareTo(BigDecimal.ZERO) > 0;
        if (!isValid) {
            log.warn("订单[{}]金额无效，已过滤", order.getId());
        }
        return isValid;
    }
}
