package com.axinger.order;

import com.axinger.shared.CustomerId;
import com.axinger.shared.OrderId;
import org.springframework.modulith.ApplicationModule;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 订单模块 - Spring Modulith 应用模块
 */
@ApplicationModule(
    allowedDependencies = {"customer-module", "inventory-module", "payment-module"},
    displayName = "订单模块"
)
public class OrderModule {

    /**
     * 订单聚合根
     */
    public record Order(
            OrderId id,
            CustomerId customerId,
            Set<OrderItem> items,
            OrderStatus status,
            double totalAmount,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {

        public Order withStatus(OrderStatus newStatus) {
            return new Order(
                    this.id,
                    this.customerId,
                    this.items,
                    newStatus,
                    this.totalAmount,
                    this.createdAt,
                    LocalDateTime.now()
            );
        }

        public boolean canBeCancelled() {
            return this.status == OrderStatus.PENDING || this.status == OrderStatus.CONFIRMED;
        }

        public boolean isPaid() {
            return this.status == OrderStatus.PAID || this.status == OrderStatus.SHIPPED;
        }
    }

    /**
     * 订单项实体
     */
    public record OrderItem(
            String productId,
            String productName,
            int quantity,
            double price
    ) {

        public double getTotalPrice() {
            return price * quantity;
        }

        public boolean isValidQuantity() {
            return quantity > 0 && quantity <= 1000;
        }
    }

    public enum OrderStatus {
        PENDING,    // 待确认
        CONFIRMED,  // 已确认
        PAID,       // 已支付
        SHIPPED,    // 已发货
        DELIVERED,  // 已送达
        CANCELLED,  // 已取消
        REFUNDED    // 已退款
    }
}