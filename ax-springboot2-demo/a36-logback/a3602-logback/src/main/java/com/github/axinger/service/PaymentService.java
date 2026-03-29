package com.github.axinger.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 支付服务 - 演示业务日志的使用
 *
 * @author xing
 */
@Service
public class PaymentService {

    /**
     * 支付业务日志
     * 对应 logback-spring.xml 中定义的 PAYMENT_LOG logger
     */
    private static final Logger paymentLog = LoggerFactory.getLogger("PAYMENT_LOG");

    /**
     * 处理支付
     *
     * @param payNo   支付单号
     * @param orderNo 订单号
     * @param amount  支付金额
     */
    public void processPayment(String payNo, String orderNo, Double amount) {
        // 记录支付处理步骤
        paymentLog.info("[PaymentService] 校验支付参数 | payNo={} | orderNo={}", payNo, orderNo);

        // 模拟参数校验
        if (amount == null || amount <= 0) {
            paymentLog.error("[PaymentService] 支付金额无效 | payNo={} | amount={}", payNo, amount);
            throw new IllegalArgumentException("支付金额必须大于0");
        }

        paymentLog.info("[PaymentService] 调用支付渠道 | payNo={} | channel=ALIPAY | amount={}", payNo, amount);

        // 模拟调用支付接口
        try {
            Thread.sleep(150); // 模拟处理时间
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        paymentLog.info("[PaymentService] 支付成功 | payNo={} | orderNo={} | transactionId=TXN{}",
                payNo, orderNo, System.currentTimeMillis());

        paymentLog.info("[PaymentService] 更新订单支付状态 | orderNo={} | status=PAID", orderNo);

        paymentLog.info("[PaymentService] 支付处理完成 | payNo={}", payNo);
    }

    /**
     * 退款
     *
     * @param payNo      支付单号
     * @param refundNo   退款单号
     * @param refundAmount 退款金额
     */
    public void refund(String payNo, String refundNo, Double refundAmount) {
        paymentLog.info("[PaymentService] 开始处理退款 | payNo={} | refundNo={} | amount={}",
                payNo, refundNo, refundAmount);

        // 模拟退款流程
        paymentLog.info("[PaymentService] 校验原支付记录 | payNo={}", payNo);
        paymentLog.info("[PaymentService] 调用退款接口 | refundNo={}", refundNo);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        paymentLog.info("[PaymentService] 退款成功 | refundNo={} | payNo={}", refundNo, payNo);
    }
}
