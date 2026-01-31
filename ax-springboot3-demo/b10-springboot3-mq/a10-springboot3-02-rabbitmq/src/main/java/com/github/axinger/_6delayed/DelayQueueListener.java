package com.github.axinger._6delayed;

import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class DelayQueueListener {

    /**
     * x-delayed-type >> direct 可以
     *
     * @param channel
     * @param message
     * @param body
     */
    @SneakyThrows
    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,
                    key = {"user.add"},
                    exchange = @Exchange(name = "delay_exchange"
                            , type = "x-delayed-message"
                            , arguments = {@Argument(name = "x-delayed-type", value = ExchangeTypes.DIRECT)}
                    )
            )
    })
    public void delay_queue1(Channel channel, Message message, @Payload Map<String, Object> body) {
        System.out.println("延时队列接收到2 = " + LocalDateTime.now());
        System.out.println("延时队列2 = " + body);

    }

    /**
     * x-delayed-type >>
     *
     * @param channel
     * @param message
     * @param body
     */
    @SneakyThrows
    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,
                    key = {"user.#"},
                    exchange = @Exchange(name = "delay_exchange",
                            type = "x-delayed-message"
//                            arguments = {@Argument(name = "x-delayed-type",value = ExchangeTypes.TOPIC)}
                    )
            )
    })
    public void delay_queue2(Channel channel, Message message, @Payload Map<String, Object> body) {
        System.out.println("延时队列接收到2 = " + LocalDateTime.now());
//        System.out.println("延时队列接收到2 message = " +message.getBody());
        System.out.println("延时队列2 = " + body);

    }


//
//    //监听消息队列
//    @RabbitListener(queues = "delay_queue")
//    public void doSth(Channel channel, Message message, @Payload String body) {
//        System.out.println("延时队列接收到 = " +LocalDateTime.now());
//        System.out.println("延时队列 = " + body);
//
////        final long deliveryTag = message.getMessageProperties().getDeliveryTag();
////        channel.basicAck(deliveryTag, true);
//    }
}
