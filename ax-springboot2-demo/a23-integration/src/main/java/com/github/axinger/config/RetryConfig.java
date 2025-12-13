package com.github.axinger.config;

import com.github.axinger.model.Order;
import com.github.axinger.model.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.math.BigDecimal;

@Slf4j
@Configuration
@EnableRetry // 启用重试功能
public class RetryConfig {
    /**
     * 订单处理重试策略：最多3次，指数退避（1s→2s→4s）
     */
    @Bean
    public RetryTemplate orderRetryTemplate() {
        RetryTemplate template = new RetryTemplate();

        // 重试策略：最多3次
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3);
        template.setRetryPolicy(retryPolicy);

        // 退避策略：指数退避，初始延迟1s，乘数2
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(1000); // 初始延迟1s
        backOffPolicy.setMultiplier(2); // 延迟倍数
        template.setBackOffPolicy(backOffPolicy);

        return template;
    }


    // 在服务激活器中应用重试
    @ServiceActivator(inputChannel = "vipOrderChannel")
    @Retryable(value = {ServiceException.class}) // 指定重试模板和异常类型
    public void handleVipOrder(Order order) {
        if (order.getAmount().compareTo(new BigDecimal("10000")) > 0) {
            throw new ServiceException("VIP大额订单处理失败，触发重试"); // 模拟失败场景
        }
        // 正常处理逻辑...
    }

    // 重试耗尽后回调（死信前置处理）
    @Recover
    public void recoverVipOrder(ServiceException e, Order order) {
        log.error("【VIP订单最终失败】订单ID：{}，原因：{}", order.getId(), e.getMessage());
        // 发送到死信通道
//        messagingTemplate.send("deadLetterChannel", MessageBuilder.withPayload(order).build());
    }

}
