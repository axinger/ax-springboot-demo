package com.github.axinger.config;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class MyConsumer {

    @RabbitListener(queues = "#{T(com.github.axinger.util.MqNames).processQueue('one')}",
            ackMode = "MANUAL")
    @RabbitHandler
    public void handleOne(@Payload String message,
                          @Header("amqp_deliveryTag") Long deliveryTag,
                          Channel channel) {
        log.info("✅✅✅[one] 收到: {}", message);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                channel.basicAck(deliveryTag, false);
            } catch (IOException e) {
                log.error("在finally块中确认第一个业务主题的延迟消息失败: {}", message, e);
            }
        }
    }

    @RabbitListener(queues = "#{T(com.github.axinger.util.MqNames).processQueue('two')}")
    public void handleTwo(Object message) {
        log.info("✅✅✅[two] 收到: {}", message);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
