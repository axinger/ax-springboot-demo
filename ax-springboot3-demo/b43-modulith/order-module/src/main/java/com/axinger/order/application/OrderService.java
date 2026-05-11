package com.axinger.order.application;

import com.axinger.order.OrderModule;
import com.axinger.order.domain.OrderRepository;
import com.axinger.shared.CustomerId;
import com.axinger.shared.OrderId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 订单应用服务 - 处理订单业务逻辑
 */
@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;

    public OrderService(OrderRepository orderRepository, ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * 创建新订单
     */
    public OrderModule.Order createOrder(CustomerId customerId, Set<OrderModule.OrderItem> items) {

        // 验证订单项
        validateOrderItems(items);

        // 计算总金额
        double totalAmount = calculateTotalAmount(items);

        // 创建订单
        OrderModule.Order order = new OrderModule.Order(
                OrderId.create(),
                customerId,
                items,
                OrderModule.OrderStatus.PENDING,
                totalAmount,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // 保存订单
        OrderModule.Order savedOrder = orderRepository.save(order);

        // 发布领域事件
        eventPublisher.publishEvent(new OrderCreatedEvent(savedOrder.id(), savedOrder.customerId(), totalAmount));

        return savedOrder;
    }

    /**
     * 确认订单
     */
    public OrderModule.Order confirmOrder(OrderId orderId) {
        OrderModule.Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("订单未找到: " + orderId));

        if (order.status() != OrderModule.OrderStatus.PENDING) {
            throw new IllegalStateException("只有待确认的订单才能确认");
        }

        OrderModule.Order confirmedOrder = order.withStatus(OrderModule.OrderStatus.CONFIRMED);
        OrderModule.Order savedOrder = orderRepository.save(confirmedOrder);

        // 发布领域事件
        eventPublisher.publishEvent(new OrderConfirmedEvent(orderId, order.customerId()));

        return savedOrder;
    }

    /**
     * 取消订单
     */
    public OrderModule.Order cancelOrder(OrderId orderId) {
        OrderModule.Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("订单未找到: " + orderId));

        if (!order.canBeCancelled()) {
            throw new IllegalStateException("订单当前状态不允许取消");
        }

        OrderModule.Order cancelledOrder = order.withStatus(OrderModule.OrderStatus.CANCELLED);
        OrderModule.Order savedOrder = orderRepository.save(cancelledOrder);

        // 发布领域事件
        eventPublisher.publishEvent(new OrderCancelledEvent(orderId, order.customerId()));

        return savedOrder;
    }

    /**
     * 查找订单
     */
    @Transactional(readOnly = true)
    public OrderModule.Order findOrderById(OrderId orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("订单未找到: " + orderId));
    }

    private void validateOrderItems(Set<OrderModule.OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("订单项不能为空");
        }

        if (items.size() > 50) {
            throw new IllegalArgumentException("单个订单最多包含50个商品");
        }

        items.forEach(item -> {
            if (!item.isValidQuantity()) {
                throw new IllegalArgumentException("商品数量无效: " + item.quantity());
            }
            if (item.price() <= 0) {
                throw new IllegalArgumentException("商品价格必须大于0");
            }
        });
    }

    private double calculateTotalAmount(Set<OrderModule.OrderItem> items) {
        return items.stream()
                .mapToDouble(OrderModule.OrderItem::getTotalPrice)
                .sum();
    }

    // 领域事件定义
    public record OrderCreatedEvent(OrderId orderId, CustomerId customerId, double amount) {}
    public record OrderConfirmedEvent(OrderId orderId, CustomerId customerId) {}
    public record OrderCancelledEvent(OrderId orderId, CustomerId customerId) {}

    // 异常类
    public static class OrderNotFoundException extends RuntimeException {
        public OrderNotFoundException(String message) {
            super(message);
        }
    }
}