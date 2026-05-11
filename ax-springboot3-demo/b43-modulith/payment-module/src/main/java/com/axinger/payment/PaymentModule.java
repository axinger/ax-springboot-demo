package com.axinger.payment;

import com.axinger.shared.OrderId;
import org.springframework.modulith.ApplicationModule;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 支付模块 - Spring Modulith 应用模块
 */
@ApplicationModule(
    allowedDependencies = {"order-module"},
    displayName = "支付模块"
)
public class PaymentModule {

    /**
     * 支付聚合根
     */
    public record Payment(
            PaymentId id,
            OrderId orderId,
            double amount,
            PaymentMethod method,
            PaymentStatus status,
            PaymentDetails details,
            LocalDateTime createdAt,
            LocalDateTime processedAt,
            String failureReason
    ) {

        public Payment withStatus(PaymentStatus newStatus) {
            return new Payment(
                    this.id,
                    this.orderId,
                    this.amount,
                    this.method,
                    newStatus,
                    this.details,
                    this.createdAt,
                    newStatus == PaymentStatus.COMPLETED ? LocalDateTime.now() : this.processedAt,
                    null
            );
        }

        public Payment withFailure(String reason) {
            return new Payment(
                    this.id,
                    this.orderId,
                    this.amount,
                    this.method,
                    PaymentStatus.FAILED,
                    this.details,
                    this.createdAt,
                    LocalDateTime.now(),
                    reason
            );
        }

        public boolean isCompleted() {
            return this.status == PaymentStatus.COMPLETED;
        }

        public boolean isPending() {
            return this.status == PaymentStatus.PENDING;
        }

        public boolean canBeRefunded() {
            return this.status == PaymentStatus.COMPLETED;
        }
    }

    /**
     * 支付ID值对象
     */
    public record PaymentId(UUID value) {

        public PaymentId {
            if (value == null) {
                throw new IllegalArgumentException("支付ID不能为空");
            }
        }

        public static PaymentId create() {
            return new PaymentId(UUID.randomUUID());
        }

        public static PaymentId fromString(String id) {
            return new PaymentId(UUID.fromString(id));
        }
    }

    /**
     * 支付方式值对象
     */
    public record PaymentMethod(String type, String displayName, boolean isActive) {

        public PaymentMethod {
            if (type == null || type.trim().isEmpty()) {
                throw new IllegalArgumentException("支付类型不能为空");
            }
            if (displayName == null || displayName.trim().isEmpty()) {
                throw new IllegalArgumentException("显示名称不能为空");
            }
        }

        public static PaymentMethod alipay() {
            return new PaymentMethod("ALIPAY", "支付宝", true);
        }

        public static PaymentMethod wechatPay() {
            return new PaymentMethod("WECHAT_PAY", "微信支付", true);
        }

        public static PaymentMethod creditCard() {
            return new PaymentMethod("CREDIT_CARD", "信用卡", true);
        }

        public static PaymentMethod bankTransfer() {
            return new PaymentMethod("BANK_TRANSFER", "银行转账", true);
        }
    }

    /**
     * 支付详情值对象
     */
    public record PaymentDetails(
            String transactionId,
            String gatewayResponse,
            String customerIp,
            String userAgent
    ) {

        public PaymentDetails {
            if (transactionId == null || transactionId.trim().isEmpty()) {
                throw new IllegalArgumentException("交易ID不能为空");
            }
        }
    }

    /**
     * 退款聚合根
     */
    public record Refund(
            PaymentId paymentId,
            double amount,
            String reason,
            RefundStatus status,
            LocalDateTime requestedAt,
            LocalDateTime processedAt
    ) {

        public Refund withStatus(RefundStatus newStatus) {
            return new Refund(
                    this.paymentId,
                    this.amount,
                    this.reason,
                    newStatus,
                    this.requestedAt,
                    newStatus == RefundStatus.COMPLETED ? LocalDateTime.now() : this.processedAt
            );
        }

        public boolean isCompleted() {
            return this.status == RefundStatus.COMPLETED;
        }
    }

    public enum PaymentStatus {
        PENDING,        // 待支付
        PROCESSING,     // 处理中
        COMPLETED,      // 已完成
        FAILED,         // 失败
        CANCELLED,      // 已取消
        EXPIRED         // 已过期
    }

    public enum RefundStatus {
        PENDING,        // 待处理
        PROCESSING,     // 处理中
        COMPLETED,      // 已完成
        FAILED,         // 失败
        REJECTED        // 已拒绝
    }
}