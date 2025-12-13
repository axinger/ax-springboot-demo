package com.github.axinger.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 第一个业务主题的延迟消息消费者
 */
@Slf4j
@Component
public class BusinessTopicOneConsumer {
    
    @Value("${spring.rabbitmq.concurrency}")
    private int concurrency;

    /**
     * 监听第一个业务主题的实际处理队列
     *
     * @param message     消息内容
     * @param deliveryTag 消息标签
     * @param channel     通道
     */
    @RabbitListener(queues = BusinessTopicOne.RECEIVE_QUEUE,
            ackMode = "MANUAL",
            concurrency = "${spring.rabbitmq.concurrency}")
    @RabbitHandler
    public void handleBusinessTopicOneMessage(@Payload String message,
                                              @Header("amqp_deliveryTag") Long deliveryTag,
                                              Channel channel) {
        boolean ackFlag = false;
        try {
            log.info("处理第一个业务主题的延迟消息: {}", message);

            // 在这里添加具体的业务逻辑处理
            processBusinessOne(message);

            // 手动确认消息
            channel.basicAck(deliveryTag, false);
            ackFlag = true;
            log.info("第一个业务主题的延迟消息处理完成，已确认: {}", message);
        } catch (Exception e) {
            log.error("处理第一个业务主题的延迟消息时发生错误: {}", message, e);
            if (!ackFlag) {
                try {
                    // 拒绝消息并重新入队
                    channel.basicNack(deliveryTag, false, true);
                    ackFlag = true;
                } catch (IOException ioException) {
                    log.error("确认第一个业务主题的延迟消息失败: {}", message, ioException);
                    // 如果basicNack失败，尝试basicAck
                    try {
                        channel.basicAck(deliveryTag, false);
                        ackFlag = true;
                    } catch (IOException ackException) {
                        log.error("在异常处理中确认第一个业务主题的延迟消息也失败了: {}", message, ackException);
                    }
                }
            }
        } finally {
            // 如果消息尚未被确认或拒绝，则在此处进行确认
            if (!ackFlag) {
                try {
                    channel.basicAck(deliveryTag, false);
                    log.info("第一个业务主题的延迟消息在finally块中确认: {}", message);
                } catch (IOException e) {
                    log.error("在finally块中确认第一个业务主题的延迟消息失败: {}", message, e);
                }
            }
        }
    }

    /**
     * 处理第一个业务主题的具体业务逻辑
     *
     * @param message 消息内容
     */
    private void processBusinessOne(String message) {
        // 实际的业务逻辑处理
        log.info("执行第一个业务主题的具体业务逻辑: {}", message);

        // 示例业务处理
        // 1. 解析消息内容
        // 2. 执行相关业务操作
        // 3. 更新数据库状态等
    }


}
