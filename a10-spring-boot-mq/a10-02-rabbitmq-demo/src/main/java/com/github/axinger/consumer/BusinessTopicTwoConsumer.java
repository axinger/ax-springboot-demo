package com.github.axinger.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 第二个业务主题的延迟消息消费者
 */
@Slf4j
@Component
public class BusinessTopicTwoConsumer {

    /**
     * 监听第二个业务主题的实际处理队列
     *
     * @param message     消息内容
     * @param deliveryTag 消息标签
     * @param channel     通道
     */
    @RabbitListener(queues = BusinessTopicTwo.RECEIVE_QUEUE,

            ackMode = "MANUAL",
            concurrency = "${spring.rabbitmq.concurrency}")
//    @RabbitHandler
    public void handleBusinessTopicTwoMessage(@Payload String message,
                                              @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag,
                                              Channel channel) {
    

        try {
            processBusinessTwo(message);


            // 使用正确的 deliveryTag 进行确认
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("error mqStr:{}", message, e);
            System.err.println("处理死信消息失败: " + e.getMessage());
            try {
                // 拒绝死信消息（不再重新入队，避免循环）
                channel.basicNack(deliveryTag, false, false);
            } catch (IOException ex) {
                System.err.println("拒绝死信消息失败: " + ex.getMessage());
            }
        }
    }

    /**
     * 处理第二个业务主题的具体业务逻辑
     *
     * @param message 消息内容
     */
    private void processBusinessTwo(String message) {
        // 实际的业务逻辑处理
        log.info("执行第二个业务主题的具体业务逻辑: {}", message);

        // 示例业务处理
        // 1. 解析消息内容
        // 2. 执行相关业务操作
        // 3. 更新数据库状态等
    }
}
