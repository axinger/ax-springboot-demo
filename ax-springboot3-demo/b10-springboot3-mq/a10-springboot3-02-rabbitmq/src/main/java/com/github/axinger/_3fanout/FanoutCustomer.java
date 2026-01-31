package com.github.axinger._3fanout;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 3、发布/订阅模式
 * <p>
 * 一个消费者将消息首先发送到交换器，交换器绑定到多个队列，然后被监听该队列的消费者所接收并消费。
 * <p>
 * ps:X表示交换器，在RabbitMQ中，交换器主要有四种类型:direct、fanout、topic、headers，这里的交换器是 fanout。下面我们会详细介绍这几种交换器。
 * 　　两个消费者获得了同一条消息。即就是，一个消息从交换机同时发送给了两个队列中，监听这两个队列的消费者消费了这个消息；
 * 如果没有队列绑定交换机，则消息将丢失。因为交换机没有存储能力，消息只能存储在队列中。
 */
@Component
public class FanoutCustomer {
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue,
            exchange = @Exchange(name = "logs", type = ExchangeTypes.FANOUT)
    ))
    public void receiveA(String message) {
        System.out.println("work messageA==" + message);
    }


//    @RabbitListener(bindings = @QueueBinding(
//            //1、队列名字 2、是否持久化 3、使用完是否自动删除
//            value = @Queue(value = "queue",durable = "true",autoDelete = "false"),
//            //1、交换机名字 2、模式类型
//            exchange = @Exchange(value = "ex",type = ExchangeTypes.TOPIC),
//            //和数据库的模糊查询一个意思  #（0个或多个），*（至少要有一个）
//            key = "#s*"
//    ))


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue,
            exchange = @Exchange(name = "logs", type = ExchangeTypes.FANOUT)
    ))
    public void receiveB(String message) {
        System.out.println("work messageB==" + message);
    }
}
