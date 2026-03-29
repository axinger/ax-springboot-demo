package com.github.axinger.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 订单服务 - 演示业务日志的使用
 *
 * @author xing
 */
@Service
public class OrderService {

    /**
     * 订单业务日志
     * 对应 logback-spring.xml 中定义的 ORDER_LOG logger
     */
    private static final Logger orderLog = LoggerFactory.getLogger("ORDER_LOG");

    /**
     * 处理订单
     *
     * @param orderNo     订单号
     * @param productName 商品名称
     * @param amount      金额
     */
    public void processOrder(String orderNo, String productName, Double amount) {
        // 记录订单处理步骤
        orderLog.info("[OrderService] 校验订单参数 | orderNo={}", orderNo);

        // 模拟参数校验
        if (amount == null || amount <= 0) {
            orderLog.error("[OrderService] 订单金额无效 | orderNo={} | amount={}", orderNo, amount);
            throw new IllegalArgumentException("订单金额必须大于0");
        }

        orderLog.info("[OrderService] 扣减库存 | orderNo={} | productName={}", orderNo, productName);

        // 模拟库存扣减
        try {
            Thread.sleep(100); // 模拟处理时间
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        orderLog.info("[OrderService] 生成订单记录 | orderNo={} | amount={}", orderNo, amount);

        // 模拟保存订单
        orderLog.info("[OrderService] 订单处理完成 | orderNo={}", orderNo);
    }

    /**
     * 取消订单
     *
     * @param orderNo 订单号
     */
    public void cancelOrder(String orderNo) {
        orderLog.info("[OrderService] 开始取消订单 | orderNo={}", orderNo);

        // 模拟取消流程
        orderLog.info("[OrderService] 回滚库存 | orderNo={}", orderNo);
        orderLog.info("[OrderService] 更新订单状态为已取消 | orderNo={}", orderNo);

        orderLog.info("[OrderService] 订单取消完成 | orderNo={}", orderNo);
    }
}