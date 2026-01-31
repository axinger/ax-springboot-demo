package com.github.axinger._4routingdirect;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 4、路由模式
 * 生产者将消息发送到direct交换器，在绑定队列和交换器的时候有一个路由key，生产者发送的消息会指定一个路由key，那么消息只会发送到相应key相同的队列，接着监听该队列的消费者消费消息。
 * <p>
 * 也就是让消费者有选择性的接收消息。
 * 路由模式，是以路由规则为导向，引导消息存入符合规则的队列中。再由队列的消费者进行消费的。
 * <p>
 * Routing 之订阅模型-Direct(直连)
 */
@Component
public class DirectCustomer {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue,
            key = {"info", "error"},
            exchange = @Exchange(name = "directs", type = ExchangeTypes.DIRECT)
    ))
    public void receive1(String message) {
        System.out.println("work message1==" + message);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue,
            key = {"info"},
            exchange = @Exchange(name = "directs", type = ExchangeTypes.DIRECT)
    ))
    public void receive2(String message) {
        System.out.println("work message2==" + message);
    }
}
