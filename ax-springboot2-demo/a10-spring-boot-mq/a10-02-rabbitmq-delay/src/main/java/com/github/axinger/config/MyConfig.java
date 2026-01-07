package com.github.axinger.config;

import com.github.axinger.util.MqNames;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class MyConfig {

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private MyTopicProperties properties;

    @PostConstruct
    public void initTopics() {
        for (MyTopicProperties.TopicConfig config : properties.getTopics()) {
            declareTopic(config.getFlag());
        }
    }

    private void declareTopic(String flag) {
        String dlxName = MqNames.dlx(flag);
        String sendQueueName = MqNames.sendQueue(flag);
        String receiveQueueName = MqNames.processQueue(flag);
        String routingKey = MqNames.routingKey(flag);

        // 1. 声明死信交换机（DLX）
        DirectExchange dlx = new DirectExchange(dlxName, true, false);
        rabbitAdmin.declareExchange(dlx);

        // 2. 声明发送队列（带死信参数）
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", dlxName);
        args.put("x-dead-letter-routing-key", routingKey);
        Queue sendQueue = QueueBuilder.durable(sendQueueName).withArguments(args).build();
        rabbitAdmin.declareQueue(sendQueue);

        // 3. 声明处理队列（实际消费队列）
        Queue processQueue = QueueBuilder.durable(receiveQueueName).build();
        rabbitAdmin.declareQueue(processQueue);

        // 4. 绑定处理队列到 DLX
        Binding binding = BindingBuilder.bind(processQueue).to(dlx).with(routingKey);
        rabbitAdmin.declareBinding(binding);

        System.out.println("✅ 已初始化业务 Topic: " + flag);
    }
}
