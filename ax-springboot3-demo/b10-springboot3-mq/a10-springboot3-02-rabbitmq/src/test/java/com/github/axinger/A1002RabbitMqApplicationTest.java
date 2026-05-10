package com.github.axinger;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class A1002RabbitMqApplicationTest {


    @Autowired
    RabbitTemplate rabbitTemplate;


    // 第一种模式(直连)
    @SneakyThrows
    @Test
    void simpleCustomerTest() {
        rabbitTemplate.convertAndSend("hello", "hello world");
    }


    @Test
    void workTest() {
        for (int i = 0; i < 20; i++) {
            rabbitTemplate.convertAndSend("work", ("provider" + i + "hello work"));
        }
    }

    // 参数1为交换机，参数2为路由key，“”表示为任意路由，参数3为消息内容
    @Test
    void fanoutTest() {
        rabbitTemplate.convertAndSend("logs", " ", "fanout provider message");
    }


    @Test
    void _4routingDirect_1() {
        rabbitTemplate.convertAndSend("directs", "error", "direct provider message");
    }

    @Test
    void _4routingDirect_2() {
        rabbitTemplate.convertAndSend("directs", "info", "direct provider message");
    }

    @Test
    void topTest() {
        rabbitTemplate.convertAndSend("topics", "user.save.findAll", "user.save.findAll 的消息");
    }

    @Test
    void topTest2() {
        rabbitTemplate.convertAndSend("topics", "user.save", "user.save 的消息");
    }


    @SneakyThrows
    @Test
    void delayQueue() {


        Map<String, Object> message = new HashMap<>();
        message.put("time", LocalDateTime.now());

        System.out.println("开始发送消息object = " + message);


        rabbitTemplate.convertAndSend(
                "delay_exchange",
                "user.add",
                message,
                messagePostProcessor -> {
                    // 配置消息的过期时间
//                    messagePostProcessor.getMessageProperties().setDelay(5 * 1000);
                    return messagePostProcessor;
                }
        );


        TimeUnit.SECONDS.sleep(15);
    }


    @SneakyThrows
    @Test
    void delayQueue2() {


        Map<String, Object> message = new HashMap<>();
        message.put("time", LocalDateTime.now());

        System.out.println("开始发送消息object = " + message);
        rabbitTemplate.convertAndSend(
                "delay_exchange",
                "user.save.xx",
                message,
                messagePostProcessor -> {
                    // 配置消息的过期时间
//                    messagePostProcessor.getMessageProperties().setDelay(5 * 1000);
                    return messagePostProcessor;
                }
        );


        TimeUnit.SECONDS.sleep(15);
    }
}
