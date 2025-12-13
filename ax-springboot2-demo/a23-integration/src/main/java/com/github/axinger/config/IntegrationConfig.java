package com.github.axinger.config;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.github.axinger.model.Order;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.MessageChannel;

import java.time.LocalDateTime;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@EnableIntegration // 启用Spring Integration基础设施

@AutoConfigureAfter(ExecutorConfig.class)
public class IntegrationConfig {
    /**
     * 订单输入通道：接收原始订单消息
     * 直连通道：单线程同步处理，适合轻量操作
     */
    @Bean
    public MessageChannel orderInputChannel(Executor orderExecutor) {


//        MessageChannels.publishSubscribe().get();
//        MessageChannels.queue().get();
//        MessageChannels.executor(orderExecutor).get();
//        MessageChannels.rendezvous().get();
//        MessageChannels.priority().get();
//        MessageChannels.flux().get();

        return MessageChannels
                .direct()
                .failover(true)
                .get();
    }

    /**
     * 有效订单通道：通过过滤器验证的订单
     */
    @Bean
    public MessageChannel validOrderChannel() {
        return new DirectChannel();
    }

    /**
     * VIP订单通道：处理VIP客户的订单
     */
    @Bean
    public MessageChannel vipOrderChannel() {
        return new DirectChannel();
    }

    /**
     * 普通订单通道：处理普通客户的订单
     */
    @Bean
    public MessageChannel normalOrderChannel() {
        return new DirectChannel();
    }

    /**
     * 默认订单通道：处理未识别类型的订单
     */
    @Bean
    public MessageChannel defaultOrderChannel() {
        return new DirectChannel();
    }

    /**
     * 转换后订单通道：经过字段补充处理的订单
     */
    @Bean
    public MessageChannel transformedOrderChannel() {
        return new DirectChannel();
    }

    /**
     * 批处理订单通道：用于批量处理订单
     */
    @Bean
    public MessageChannel batchOrderChannel() {
        return new DirectChannel();
    }

    /**
     * 异步订单处理通道：使用自定义线程池处理订单
     *
     * @param orderExecutor 自定义订单处理线程池
     * @return ExecutorChannel
     */
    @Bean
    public MessageChannel asyncOrderChannel(Executor orderExecutor) {
        return new ExecutorChannel(orderExecutor); // 使用orderExecutor线程池
    }

    /**
     * 异步处理通道：基于线程池处理，适合耗时操作
     * 核心线程5，最大10，队列容量100
     */
    @Bean
    public MessageChannel asyncProcessingChannel() {
        return new ExecutorChannel(Executors.newFixedThreadPool(5,
                new ThreadFactory() {
                    private final AtomicInteger counter = new AtomicInteger(1);

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "order-processor-" + counter.getAndIncrement());
                    }
                }));
    }

    /**
     * 死信通道：接收最终处理失败的消息
     */
    @Bean
    public MessageChannel deadLetterChannel() {
        return new DirectChannel();
    }


    /**
     * 批处理订单流程：按用户ID聚合订单，满足条件后批量处理
     * 聚合条件：
     * 1. 同一用户订单数量达到10条
     * 2. 或者等待时间超过5秒
     *
     * @return IntegrationFlow
     */
    @Bean
    public IntegrationFlow batchOrderFlow() {
        return IntegrationFlows.from("batchOrderChannel")
                .aggregate(a -> a
                        .correlationStrategy(m -> ((Order) m.getPayload()).getUserId()) // 按用户ID聚合
                        .releaseStrategy(g -> {
                                    Long timestamp = g.getOne().getHeaders().getTimestamp();
                                    // 如果没有时间戳，则只按数量判断
                                    if (timestamp == null) {
                                        return g.size() >= 10;
                                    }
                                    LocalDateTime localDateTime = LocalDateTimeUtil.of(timestamp);
                                    // 判断是否超过5秒
                                    boolean isTimeout = LocalDateTimeUtil.between(localDateTime, LocalDateTime.now()).getSeconds() > 5;
                                    // 满足10条或5s超时则释放
                                    return g.size() >= 10 || isTimeout;
                                }
                        )
                        .sendPartialResultOnExpiry(true)) // 超时发送部分结果
                .handle("batchOrderService", "processBatch") // 批处理方法
//                .handle("consumerMessageHandler") // 批处理方法
                .get();
    }

}
