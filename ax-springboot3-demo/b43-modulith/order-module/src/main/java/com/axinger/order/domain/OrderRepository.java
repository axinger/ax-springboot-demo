package com.axinger.order.domain;

import com.axinger.order.OrderModule;
import com.axinger.shared.OrderId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 订单仓储接口
 */
@Repository
public interface OrderRepository extends JpaRepository<OrderModule.Order, OrderId> {

    Optional<OrderModule.Order> findByCustomerIdAndStatus(
            com.axinger.shared.CustomerId customerId,
            OrderModule.OrderStatus status
    );

    boolean existsByCustomerIdAndStatus(
            com.axinger.shared.CustomerId customerId,
            OrderModule.OrderStatus status
    );
}