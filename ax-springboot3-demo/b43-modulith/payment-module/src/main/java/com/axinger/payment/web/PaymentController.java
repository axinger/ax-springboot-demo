package com.axinger.payment.web;

import com.axinger.payment.PaymentModule;
import com.axinger.payment.application.PaymentService;
import com.axinger.shared.OrderId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * 支付控制器 - REST API 端点
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * 创建支付
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse createPayment(@RequestBody CreatePaymentRequest request) {
        OrderId orderId = OrderId.fromString(request.orderId());

        PaymentModule.PaymentMethod paymentMethod;
        switch (request.paymentMethod().toUpperCase()) {
            case "ALIPAY" -> paymentMethod = PaymentModule.PaymentMethod.alipay();
            case "WECHAT_PAY" -> paymentMethod = PaymentModule.PaymentMethod.wechatPay();
            case "CREDIT_CARD" -> paymentMethod = PaymentModule.PaymentMethod.creditCard();
            case "BANK_TRANSFER" -> paymentMethod = PaymentModule.PaymentMethod.bankTransfer();
            default -> throw new IllegalArgumentException("不支持的支付方式: " + request.paymentMethod());
        }

        PaymentModule.Payment payment = paymentService.createPayment(orderId, request.amount(), paymentMethod);

        return toPaymentResponse(payment);
    }

    /**
     * 处理支付
     */
    @PostMapping("/{paymentId}/process")
    public PaymentResponse processPayment(@PathVariable String paymentId) {
        PaymentModule.PaymentId id = PaymentModule.PaymentId.fromString(paymentId);
        PaymentModule.Payment payment = paymentService.processPayment(id);

        return toPaymentResponse(payment);
    }

    /**
     * 取消支付
     */
    @PostMapping("/{paymentId}/cancel")
    public PaymentResponse cancelPayment(@PathVariable String paymentId) {
        PaymentModule.PaymentId id = PaymentModule.PaymentId.fromString(paymentId);
        PaymentModule.Payment payment = paymentService.cancelPayment(id);

        return toPaymentResponse(payment);
    }

    /**
     * 创建退款
     */
    @PostMapping("/{paymentId}/refund")
    @ResponseStatus(HttpStatus.CREATED)
    public RefundResponse createRefund(
            @PathVariable String paymentId,
            @RequestBody CreateRefundRequest request) {

        PaymentModule.PaymentId id = PaymentModule.PaymentId.fromString(paymentId);
        PaymentModule.Refund refund = paymentService.createRefund(id, request.amount(), request.reason());

        return toRefundResponse(refund);
    }

    /**
     * 获取支付详情
     */
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable String paymentId) {
        PaymentModule.PaymentId id = PaymentModule.PaymentId.fromString(paymentId);
        Optional<PaymentModule.Payment> payment = paymentService.findPaymentById(id);

        return payment.map(p -> ResponseEntity.ok(toPaymentResponse(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 根据订单ID获取支付
     */
    @GetMapping("/by-order/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentByOrderId(@PathVariable String orderId) {
        OrderId id = OrderId.fromString(orderId);
        Optional<PaymentModule.Payment> payment = paymentService.findPaymentByOrderId(id);

        return payment.map(p -> ResponseEntity.ok(toPaymentResponse(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 获取支付统计信息
     */
    @GetMapping("/stats")
    public PaymentStatisticsResponse getPaymentStatistics() {
        PaymentService.PaymentStatistics stats = paymentService.getPaymentStatistics();

        return new PaymentStatisticsResponse(
                stats.totalPayments(),
                stats.completedPayments(),
                stats.pendingPayments(),
                stats.failedPayments()
        );
    }

    // 响应对象转换方法
    private PaymentResponse toPaymentResponse(PaymentModule.Payment payment) {
        return new PaymentResponse(
                payment.id().toString(),
                payment.orderId().toString(),
                payment.amount(),
                payment.method().type(),
                payment.method().displayName(),
                payment.status().name(),
                payment.details().transactionId(),
                payment.createdAt(),
                payment.processedAt(),
                payment.failureReason()
        );
    }

    private RefundResponse toRefundResponse(PaymentModule.Refund refund) {
        return new RefundResponse(
                refund.paymentId().toString(),
                refund.amount(),
                refund.reason(),
                refund.status().name(),
                refund.requestedAt(),
                refund.processedAt()
        );
    }

    // 请求/响应对象
    public record CreatePaymentRequest(
            String orderId,
            double amount,
            String paymentMethod
    ) {}

    public record CreateRefundRequest(
            double amount,
            String reason
    ) {}

    public record PaymentResponse(
            String paymentId,
            String orderId,
            double amount,
            String paymentMethod,
            String paymentMethodDisplayName,
            String status,
            String transactionId,
            java.time.LocalDateTime createdAt,
            java.time.LocalDateTime processedAt,
            String failureReason
    ) {}

    public record RefundResponse(
            String paymentId,
            double amount,
            String reason,
            String status,
            java.time.LocalDateTime requestedAt,
            java.time.LocalDateTime processedAt
    ) {}

    public record PaymentStatisticsResponse(
            long totalPayments,
            long completedPayments,
            long pendingPayments,
            long failedPayments
    ) {}
}