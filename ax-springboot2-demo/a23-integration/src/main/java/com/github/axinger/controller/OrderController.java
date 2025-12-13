package com.github.axinger.controller;

import com.github.axinger.config.OrderGateway;
import com.github.axinger.model.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// 2. HTTP接口触发（外部调用入口）
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderGateway orderGateway;

    public OrderController(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    @GetMapping("/1")
    public ResponseEntity<String> test1() {
        return ResponseEntity.ok("订单已接收，处理中");
    }

    @PostMapping("/submit")
    public ResponseEntity<String> submitOrder(@RequestBody Order order) {
        System.out.println("接收到订单：" + order);
        orderGateway.receiveOrder(order); // 发送到消息网关
        return ResponseEntity.ok("订单已接收，处理中");
    }
}
