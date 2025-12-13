package com.github.axinger._1simple;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
public class Controller {


    @Autowired
    RabbitTemplate rabbitTemplate;

    // 第一种模式(直连)
    @GetMapping("/01")
    void simpleCustomerTest() {
        rabbitTemplate.convertAndSend("hello", "hello world");
    }


//    @RabbitHandler
//    @RabbitListener(queuesToDeclare = @Queue(value = "hello"))
//    public void receive(String message) {
//        log.info("方式1.简单模式message== {}", message);
//    }

    //    @Retryable(
//            value = {Exception.class}, // 需要重试的异常类型
//            maxAttempts = 3, // 最大重试次数
//            backoff = @Backoff(delay = 1000) // 重试间隔时间（毫秒）
//    )
    @RabbitListener(queuesToDeclare = @Queue(value = "hello"))
    public void receive(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            log.info("方式1.简单模式message== {}", message);
            // 处理消息
            channel.basicAck(deliveryTag, false); // 手动确认消息
        } catch (Exception e) {
            log.error("处理消息时发生异常: {}", e.getMessage());
            channel.basicNack(deliveryTag, false, true); // 拒绝消息并重新入队
        }
    }
}
