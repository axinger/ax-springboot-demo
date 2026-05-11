package com.axinger.order.application;

import com.axinger.order.OrderModule;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        // Given
        CustomerId customerId = CustomerId.create();
        Set<OrderModule.OrderItem> items = Set.of(
            new OrderModule.OrderItem("PROD-001", "iPhone 15", 1, 6999.00)
        );

        OrderModule.Order expectedOrder = new OrderModule.Order(
            OrderId.create(),
            customerId,
            items,
            OrderModule.OrderStatus.PENDING,
            6999.00,
            java.time.LocalDateTime.now(),
            java.time.LocalDateTime.now()
        );

        when(orderRepository.save(any(OrderModule.Order.class))).thenReturn(expectedOrder);

        // When
        OrderModule.Order result = orderService.createOrder(customerId, items);

        // Then
        assertNotNull(result);
        assertEquals(OrderModule.OrderStatus.PENDING, result.status());
        assertEquals(6999.00, result.totalAmount());
        verify(eventPublisher, times(1)).publishEvent(any(OrderService.OrderCreatedEvent.class));
    }

    @Test
    void shouldThrowExceptionWhenItemsAreEmpty() {
        // Given
        CustomerId customerId = CustomerId.create();
        Set<OrderModule.OrderItem> emptyItems = Set.of();

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(customerId, emptyItems);
        });
    }

    @Test
    void shouldConfirmOrderSuccessfully() {
        // Given
        OrderId orderId = OrderId.create();
        CustomerId customerId = CustomerId.create();
        Set<OrderModule.OrderItem> items = Set.of(
            new OrderModule.OrderItem("PROD-001", "iPhone 15", 1, 6999.00)
        );

        OrderModule.Order pendingOrder = new OrderModule.Order(
            orderId,
            customerId,
            items,
            OrderModule.OrderStatus.PENDING,
            6999.00,
            java.time.LocalDateTime.now(),
            java.time.LocalDateTime.now()
        );

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(pendingOrder));
        when(orderRepository.save(any(OrderModule.Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        OrderModule.Order result = orderService.confirmOrder(orderId);

        // Then
        assertNotNull(result);
        assertEquals(OrderModule.OrderStatus.CONFIRMED, result.status());
        verify(eventPublisher, times(1)).publishEvent(any(OrderService.OrderConfirmedEvent.class));
    }

    @Test
    void shouldThrowExceptionWhenConfirmingNonPendingOrder() {
        // Given
        OrderId orderId = OrderId.create();
        CustomerId customerId = CustomerId.create();
        Set<OrderModule.OrderItem> items = Set.of();

        OrderModule.Order confirmedOrder = new OrderModule.Order(
            orderId,
            customerId,
            items,
            OrderModule.OrderStatus.CONFIRMED,
            0.0,
            java.time.LocalDateTime.now(),
            java.time.LocalDateTime.now()
        );

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(confirmedOrder));

        // When & Then
        assertThrows(IllegalStateException.class, () -> {
            orderService.confirmOrder(orderId);
        });
    }

    @Test
    void shouldCancelOrderSuccessfully() {
        // Given
        OrderId orderId = OrderId.create();
        CustomerId customerId = CustomerId.create();
        Set<OrderModule.OrderItem> items = Set.of();

        OrderModule.Order pendingOrder = new OrderModule.Order(
            orderId,
            customerId,
            items,
            OrderModule.OrderStatus.PENDING,
            0.0,
            java.time.LocalDateTime.now(),
            java.time.LocalDateTime.now()
        );

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(pendingOrder));
        when(orderRepository.save(any(OrderModule.Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        OrderModule.Order result = orderService.cancelOrder(orderId);

        // Then
        assertNotNull(result);
        assertEquals(OrderModule.OrderStatus.CANCELLED, result.status());
        verify(eventPublisher, times(1)).publishEvent(any(OrderService.OrderCancelledEvent.class));
    }

    @Test
    void shouldFindOrderById() {
        // Given
        OrderId orderId = OrderId.create();
        CustomerId customerId = CustomerId.create();
        Set<OrderModule.OrderItem> items = Set.of();

        OrderModule.Order expectedOrder = new OrderModule.Order(
            orderId,
            customerId,
            items,
            OrderModule.OrderStatus.PENDING,
            0.0,
            java.time.LocalDateTime.now(),
            java.time.LocalDateTime.now()
        );

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(expectedOrder));

        // When
        OrderModule.Order result = orderService.findOrderById(orderId);

        // Then
        assertNotNull(result);
        assertEquals(orderId, result.id());
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        // Given
        OrderId orderId = OrderId.create();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(OrderService.OrderNotFoundException.class, () -> {
            orderService.findOrderById(orderId);
        });
    }
}
