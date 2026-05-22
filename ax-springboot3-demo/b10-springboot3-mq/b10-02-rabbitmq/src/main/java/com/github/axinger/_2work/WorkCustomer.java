package com.github.axinger._2work;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 2、work 模式
 * <p>
 * 一个生产者对应多个消费者，但是一条消息只能有一个消费者获得消息！！！
 * 轮询分发就是将消息队列中的消息，依次发送给所有消费者。一个消息只能被一个消费者获取。
 */
@Component
public class WorkCustomer {
    @RabbitListener(queuesToDeclare = @Queue("work"))
    public void receive1(String message) {
        System.out.println("work message1==" + message);
    }

    @RabbitListener(queuesToDeclare = @Queue("work"))
    public void receive2(String message) {
        System.out.println("work message2==" + message);
    }
}
