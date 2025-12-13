package com.github.axinger.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 延迟消息发送服务
 * 使用RabbitMQ的TTL和死信队列机制实现延迟消息
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DelayMessageSender {


    private final RabbitTemplate rabbitTemplate;


    /**
     * 发送第一个业务主题的延迟消息
     *
     * @param message   消息内容
     * @param delayTime 延迟时间(毫秒)
     */
    public void sendBusinessTopicOneDelayMessage(String message, long delayTime) {
        log.info("发送第一个业务主题的延迟消息: message={}, delayTime={}ms", message, delayTime);
        try {
            rabbitTemplate.convertAndSend(
                    BusinessTopicOne.SEND_QUEUE,
                    message,
                    msg -> {
                        msg.getMessageProperties().setExpiration(String.valueOf(delayTime));
                        log.info("第一个业务主题的延迟消息已发送: {}", new SimpleDateFormat().format(new Date()));
                        return msg;
                    }
            );
        } catch (Exception e) {
            log.error("发送第一个业务主题的延迟消息失败: message={}, delayTime={}", message, delayTime, e);
        }
    }
    
    /**
     * 发送第二个业务主题的延迟消息 - 修正版本
     *
     * @param message   消息内容
     * @param delayTime 延迟时间(毫秒)
     */
    public void sendBusinessTopicTwoDelayMessage(String message, long delayTime) {
        log.info("发送第二个业务主题的延迟消息: message={}, delayTime={}ms", message, delayTime);
        try {
            // 重要：发送到业务交换机，路由到延迟队列
            rabbitTemplate.convertAndSend(
                    BusinessTopicTwo.SEND_QUEUE,
                    message,
                    msg -> {
                        // 设置消息级别的TTL
                        msg.getMessageProperties().setExpiration(String.valueOf(delayTime));
                        log.info("第二个业务主题的延迟消息已发送，预计{}ms后处理", delayTime);
                        return msg;
                    }
            );
        } catch (Exception e) {
            log.error("发送第二个业务主题的延迟消息失败: message={}, delayTime={}", message, delayTime, e);
        }
    }

    /**
     * 发送指定队列的延迟消息
     *
     * @param queueName 队列名称
     * @param message   消息内容
     * @param delayTime 延迟时间(毫秒)
     */
    public void sendDelayMessage(String queueName, String message, long delayTime) {
        log.info("发送指定队列的延迟消息: queue={}, message={}, delayTime={}ms", queueName, message, delayTime);
        try {
            rabbitTemplate.convertAndSend(
                    queueName,
                    message,
                    msg -> {
                        msg.getMessageProperties().setExpiration(String.valueOf(delayTime));
                        log.info("指定队列的延迟消息已发送: {}", new SimpleDateFormat().format(new Date()));
                        return msg;
                    }
            );
        } catch (Exception e) {
            log.error("发送指定队列的延迟消息失败: queue={}, message={}, delayTime={}", queueName, message, delayTime, e);
        }
    }
}
