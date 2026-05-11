package com.axinger.order.web;

import com.axinger.order.OrderModule;
import com.axinger.order.application.OrderService;
import com.axinger.shared.CustomerId;
import com.axinger.shared.OrderId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 订单控制器 - REST API 端点
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 创建新订单
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@RequestBody CreateOrderRequest request) {
        CustomerId customerId = CustomerId.fromString(request.customerId());
        Set<OrderModule.OrderItem> items = request.items().stream()
                .map(item -> new OrderModule.OrderItem(
                        item.productId(),
                        item.productName(),
                        item.quantity(),
                        item.price()
                ))
                .collect(Collectors.toSet());

        OrderModule.Order order = orderService.createOrder(customerId, items);

        return new OrderResponse(
                order.id().toString(),
                order.customerId().toString(),
                order.status().name(),
                order.totalAmount()
        );
    }

    /**
     * 确认订单
     */
    @PostMapping("/{orderId}/confirm")
    public OrderResponse confirmOrder(@PathVariable String orderId) {
        OrderId id = OrderId.fromString(orderId);
        OrderModule.Order order = orderService.confirmOrder(id);

        return new OrderResponse(
                order.id().toString(),
                order.customerId().toString(),
                order.status().name(),
                order.totalAmount()
        );
    }

    /**
     * 取消订单
     */
    @PostMapping("/{orderId}/cancel")
    public OrderResponse cancelOrder(@PathVariable String orderId) {
        OrderId id = OrderId.fromString(orderId);
        OrderModule.Order order = orderService.cancelOrder(id);

        return new OrderResponse(
                order.id().toString(),
                order.customerId().toString(),
                order.status().name(),
                order.totalAmount()
        );
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/{orderId}")
    public OrderResponse getOrder(@PathVariable String orderId) {
        OrderId id = OrderId.fromString(orderId);
        OrderModule.Order order = orderService.findOrderById(id);

        return new OrderResponse(
                order.id().toString(),
                order.customerId().toString(),
                order.status().name(),
                order.totalAmount()
        );
    }

    // 请求/响应对象
    public record CreateOrderRequest(
            String customerId,
            Set<OrderItemRequest> items
    ) {}

    public record OrderItemRequest(
            String productId,
            String productName,
            int quantity,
            double price
    ) {}

    public record OrderResponse(
            String orderId,
            String customerId,
            String status,
            double totalAmount
    ) {}
}