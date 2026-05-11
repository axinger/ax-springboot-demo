package com.axinger.payment.domain;

import com.axinger.payment.PaymentModule;
import com.axinger.shared.OrderId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 支付仓储接口
 */
@Repository
public interface PaymentRepository extends JpaRepository<PaymentModule.Payment, PaymentModule.PaymentId> {

    /**
     * 根据订单ID查找支付
     */
    Optional<PaymentModule.Payment> findByOrderId(OrderId orderId);

    /**
     * 根据支付状态查找支付
     */
    List<PaymentModule.Payment> findByStatus(PaymentModule.PaymentStatus status);

    /**
     * 根据订单ID和状态查找支付
     */
    Optional<PaymentModule.Payment> findByOrderIdAndStatus(OrderId orderId, PaymentModule.PaymentStatus status);

    /**
     * 计算特定状态的支付数量
     */
    long countByStatus(PaymentModule.PaymentStatus status);

    /**
     * 查找指定时间范围内的支付
     */
    List<PaymentModule.Payment> findByCreatedAtBetween(
            java.time.LocalDateTime startDate,
            java.time.LocalDateTime endDate
    );

    /**
     * 查找可以退款的支付
     */
    List<PaymentModule.Payment> findByStatusAndAmountGreaterThan(PaymentModule.PaymentStatus status, double minAmount);

    /**
     * 检查订单是否已有支付记录
     */
    boolean existsByOrderId(OrderId orderId);
}