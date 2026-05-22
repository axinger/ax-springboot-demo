package com.github.axinger.seata.controller;

import com.github.axinger.seata.domain.Order;
import com.github.axinger.seata.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName OrderController.java
 * @Description TODO
 * @createTime 2021年12月19日 00:52:00
 */

@RestController
public class OrderController {

    @Autowired
    OrderService orderService;


    @GetMapping("/test")
    public Object test() {
        return "test-OrderController";
    }


    @GetMapping("/addOrder/{productId}")
    public Object add(@PathVariable Long productId) {
        final Order order = Order.builder()
                .userId(1L)
                .productId(productId)
                .count(10)
                .money(BigDecimal.valueOf(50.00))
                .status(0)
                .build();
        return orderService.create(order);
    }
}
