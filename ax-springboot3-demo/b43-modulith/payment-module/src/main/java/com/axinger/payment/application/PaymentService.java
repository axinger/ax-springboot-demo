package com.axinger.payment.application;

import com.axinger.payment.PaymentModule;
import com.axinger.payment.domain.PaymentRepository;
import com.axinger.shared.OrderId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * 支付应用服务 - 处理支付业务逻辑
 */
@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ApplicationEventPublisher eventPublisher;

    public PaymentService(PaymentRepository paymentRepository, ApplicationEventPublisher eventPublisher) {
        this.paymentRepository = paymentRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * 创建支付
     */
    public PaymentModule.Payment createPayment(
            OrderId orderId,
            double amount,
            PaymentModule.PaymentMethod paymentMethod) {

        if (amount <= 0) {
            throw new IllegalArgumentException("支付金额必须大于0");
        }

        if (!paymentMethod.isActive()) {
            throw new IllegalArgumentException("支付方式不可用");
        }

        PaymentModule.PaymentDetails details = new PaymentModule.PaymentDetails(
                generateTransactionId(),
                "",
                "unknown",
                "unknown"
        );

        PaymentModule.Payment payment = new PaymentModule.Payment(
                PaymentModule.PaymentId.create(),
                orderId,
                amount,
                paymentMethod,
                PaymentModule.PaymentStatus.PENDING,
                details,
                LocalDateTime.now(),
                null,
                null
        );

        PaymentModule.Payment savedPayment = paymentRepository.save(payment);

        // 发布支付创建事件
        eventPublisher.publishEvent(new PaymentCreatedEvent(savedPayment.id(), orderId, amount));

        return savedPayment;
    }

    /**
     * 处理支付
     */
    public PaymentModule.Payment processPayment(PaymentModule.PaymentId paymentId) {
        PaymentModule.Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("支付未找到: " + paymentId));

        if (payment.status() != PaymentModule.PaymentStatus.PENDING) {
            throw new IllegalStateException("只有待支付的订单才能处理");
        }

        // 模拟支付处理逻辑
        PaymentModule.PaymentStatus newStatus = simulatePaymentProcessing();

        PaymentModule.Payment processedPayment;
        if (newStatus == PaymentModule.PaymentStatus.COMPLETED) {
            processedPayment = payment.withStatus(newStatus);
            // 发布支付完成事件
            eventPublisher.publishEvent(new PaymentCompletedEvent(paymentId, payment.orderId(), payment.amount()));
        } else {
            processedPayment = payment.withFailure("支付处理失败");
            // 发布支付失败事件
            eventPublisher.publishEvent(new PaymentFailedEvent(paymentId, payment.orderId(), "支付处理失败"));
        }

        return paymentRepository.save(processedPayment);
    }

    /**
     * 取消支付
     */
    public PaymentModule.Payment cancelPayment(PaymentModule.PaymentId paymentId) {
        PaymentModule.Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("支付未找到: " + paymentId));

        if (payment.status() != PaymentModule.PaymentStatus.PENDING) {
            throw new IllegalStateException("只有待支付的订单才能取消");
        }

        PaymentModule.Payment cancelledPayment = payment.withStatus(PaymentModule.PaymentStatus.CANCELLED);
        PaymentModule.Payment savedPayment = paymentRepository.save(cancelledPayment);

        // 发布支付取消事件
        eventPublisher.publishEvent(new PaymentCancelledEvent(paymentId, payment.orderId()));

        return savedPayment;
    }

    /**
     * 创建退款
     */
    public PaymentModule.Refund createRefund(
            PaymentModule.PaymentId paymentId,
            double amount,
            String reason) {

        PaymentModule.Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("支付未找到: " + paymentId));

        if (!payment.canBeRefunded()) {
            throw new IllegalStateException("该支付不能退款");
        }

        if (amount <= 0 || amount > payment.amount()) {
            throw new IllegalArgumentException("退款金额无效");
        }

        PaymentModule.Refund refund = new PaymentModule.Refund(
                paymentId,
                amount,
                reason,
                PaymentModule.RefundStatus.PENDING,
                LocalDateTime.now(),
                null
        );

        // 这里应该有退款仓储，暂时直接处理
        PaymentModule.Refund processedRefund = refund.withStatus(PaymentModule.RefundStatus.COMPLETED);

        // 发布退款创建事件
        eventPublisher.publishEvent(new RefundCreatedEvent(paymentId, amount, reason));

        return processedRefund;
    }

    /**
     * 根据ID查找支付
     */
    @Transactional(readOnly = true)
    public Optional<PaymentModule.Payment> findPaymentById(PaymentModule.PaymentId paymentId) {
        return paymentRepository.findById(paymentId);
    }

    /**
     * 根据订单ID查找支付
     */
    @Transactional(readOnly = true)
    public Optional<PaymentModule.Payment> findPaymentByOrderId(OrderId orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

    /**
     * 获取支付统计信息
     */
    @Transactional(readOnly = true)
    public PaymentStatistics getPaymentStatistics() {
        long totalPayments = paymentRepository.count();
        long completedPayments = paymentRepository.countByStatus(PaymentModule.PaymentStatus.COMPLETED);
        long pendingPayments = paymentRepository.countByStatus(PaymentModule.PaymentStatus.PENDING);
        long failedPayments = paymentRepository.countByStatus(PaymentModule.PaymentStatus.FAILED);

        return new PaymentStatistics(totalPayments, completedPayments, pendingPayments, failedPayments);
    }

    // 模拟支付处理逻辑
    private PaymentModule.PaymentStatus simulatePaymentProcessing() {
        // 模拟90%成功率
        return Math.random() < 0.9 ? PaymentModule.PaymentStatus.COMPLETED : PaymentModule.PaymentStatus.FAILED;
    }

    // 生成交易ID
    private String generateTransactionId() {
        return "TXN" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
    }

    // 领域事件定义
    public record PaymentCreatedEvent(PaymentModule.PaymentId paymentId, OrderId orderId, double amount) {}
    public record PaymentCompletedEvent(PaymentModule.PaymentId paymentId, OrderId orderId, double amount) {}
    public record PaymentFailedEvent(PaymentModule.PaymentId paymentId, OrderId orderId, String reason) {}
    public record PaymentCancelledEvent(PaymentModule.PaymentId paymentId, OrderId orderId) {}
    public record RefundCreatedEvent(PaymentModule.PaymentId paymentId, double amount, String reason) {}

    // 统计信息
    public record PaymentStatistics(
            long totalPayments,
            long completedPayments,
            long pendingPayments,
            long failedPayments
    ) {}

    // 异常类
    public static class PaymentNotFoundException extends RuntimeException {
        public PaymentNotFoundException(String message) {
            super(message);
        }
    }
}