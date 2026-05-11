package com.axinger.order;

import com.axinger.order.application.OrderService;
import com.axinger.order.domain.OrderRepository;
import com.axinger.shared.CustomerId;
import com.axinger.shared.OrderId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 订单服务单元测试
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository, eventPublisher);
    }

    @Test
    void shouldCreateOrderSuccessfully() {
        // 准备测试数据
        CustomerId customerId = CustomerId.create();
        Set<OrderModule.OrderItem> items = Set.of(
            new OrderModule.OrderItem("PROD-001", "iPhone 15", 1, 6999.0)
        );

        OrderModule.Order expectedOrder = new OrderModule.Order(
            OrderId.create(),
            customerId,
            items,
            OrderModule.OrderStatus.PENDING,
            6999.0,
            java.time.LocalDateTime.now(),
            java.time.LocalDateTime.now()
        );

        when(orderRepository.save(any(OrderModule.Order.class))).thenReturn(expectedOrder);

        // 执行测试
        OrderModule.Order result = orderService.createOrder(customerId, items);

        // 验证结果
        assertNotNull(result);
        assertEquals(customerId, result.customerId());
        assertEquals(OrderModule.OrderStatus.PENDING, result.status());
        assertEquals(6999.0, result.totalAmount());
        verify(orderRepository).save(any(OrderModule.Order.class));
        verify(eventPublisher).publishEvent(any(OrderService.OrderCreatedEvent.class));
    }

    @Test
    void shouldConfirmOrderSuccessfully() {
        // 准备测试数据
        OrderId orderId = OrderId.create();
        CustomerId customerId = CustomerId.create();
        Set<OrderModule.OrderItem> items = Set.of(
            new OrderModule.OrderItem("PROD-001", "iPhone 15", 1, 6999.0)
        );

        OrderModule.Order pendingOrder = new OrderModule.Order(
            orderId,
            customerId,
            items,
            OrderModule.OrderStatus.PENDING,
            6999.0,
            java.time.LocalDateTime.now(),
            java.time.LocalDateTime.now()
        );

        OrderModule.Order confirmedOrder = pendingOrder.withStatus(OrderModule.OrderStatus.CONFIRMED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(pendingOrder));
        when(orderRepository.save(any(OrderModule.Order.class))).thenReturn(confirmedOrder);

        // 执行测试
        OrderModule.Order result = orderService.confirmOrder(orderId);

        // 验证结果
        assertNotNull(result);
        assertEquals(OrderModule.OrderStatus.CONFIRMED, result.status());
        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(any(OrderModule.Order.class));
        verify(eventPublisher).publishEvent(any(OrderService.OrderConfirmedEvent.class));
    }

    @Test
    void shouldThrowExceptionWhenConfirmNonPendingOrder() {
        // 准备测试数据
        OrderId orderId = OrderId.create();
        CustomerId customerId = CustomerId.create();
        Set<OrderModule.OrderItem> items = Set.of(
            new OrderModule.OrderItem("PROD-001", "iPhone 15", 1, 6999.0)
        );

        OrderModule.Order confirmedOrder = new OrderModule.Order(
            orderId,
            customerId,
            items,
            OrderModule.OrderStatus.CONFIRMED,
            6999.0,
            java.time.LocalDateTime.now(),
            java.time.LocalDateTime.now()
        );

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(confirmedOrder));

        // 执行测试并验证异常
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> orderService.confirmOrder(orderId)
        );

        assertEquals("只有待确认的订单才能确认", exception.getMessage());
        verify(orderRepository).findById(orderId);
        verify(orderRepository, never()).save(any(OrderModule.Order.class));
    }

    @Test
    void shouldCancelOrderSuccessfully() {
        // 准备测试数据
        OrderId orderId = OrderId.create();
        CustomerId customerId = CustomerId.create();
        Set<OrderModule.OrderItem> items = Set.of(
            new OrderModule.OrderItem("PROD-001", "iPhone 15", 1, 6999.0)
        );

        OrderModule.Order pendingOrder = new OrderModule.Order(
            orderId,
            customerId,
            items,
            OrderModule.OrderStatus.PENDING,
            6999.0,
            java.time.LocalDateTime.now(),
            java.time.LocalDateTime.now()
        );

        OrderModule.Order cancelledOrder = pendingOrder.withStatus(OrderModule.OrderStatus.CANCELLED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(pendingOrder));
        when(orderRepository.save(any(OrderModule.Order.class))).thenReturn(cancelledOrder);

        // 执行测试
        OrderModule.Order result = orderService.cancelOrder(orderId);

        // 验证结果
        assertNotNull(result);
        assertEquals(OrderModule.OrderStatus.CANCELLED, result.status());
        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(any(OrderModule.Order.class));
        verify(eventPublisher).publishEvent(any(OrderService.OrderCancelledEvent.class));
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        // 准备测试数据
        OrderId orderId = OrderId.create();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // 执行测试并验证异常
        OrderService.OrderNotFoundException exception = assertThrows(
            OrderService.OrderNotFoundException.class,
            () -> orderService.findOrderById(orderId)
        );

        assertTrue(exception.getMessage().contains("订单未找到"));
        verify(orderRepository).findById(orderId);
    }

    @Test
    void shouldValidateOrderItems() {
        // 准备无效的测试数据
        CustomerId customerId = CustomerId.create();
        Set<OrderModule.OrderItem> emptyItems = Set.of();

        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> orderService.createOrder(customerId, emptyItems)
        );

        assertEquals("订单项不能为空", exception.getMessage());
    }

    @Test
    void shouldCalculateTotalAmount() {
        // 准备测试数据
        CustomerId customerId = CustomerId.create();
        Set<OrderModule.OrderItem> items = Set.of(
            new OrderModule.OrderItem("PROD-001", "iPhone 15", 2, 6999.0),
            new OrderModule.OrderItem("PROD-002", "AirPods", 1, 1299.0)
        );

        OrderModule.Order expectedOrder = new OrderModule.Order(
            OrderId.create(),
            customerId,
            items,
            OrderModule.OrderStatus.PENDING,
            15297.0, // 2*6999 + 1*1299
            java.time.LocalDateTime.now(),
            java.time.LocalDateTime.now()
        );

        when(orderRepository.save(any(OrderModule.Order.class))).thenReturn(expectedOrder);

        // 执行测试
        OrderModule.Order result = orderService.createOrder(customerId, items);

        // 验证结果
        assertEquals(15297.0, result.totalAmount());
        verify(orderRepository).save(any(OrderModule.Order.class));
    }
}