package com.github.axinger.consumer;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class BusinessTopicTwo {

    /// 统一定义后缀标志
    public static final String FLAG = "two";

    /// 以下都是固定值==================================================


    public static final String DLX = "business.topic." + FLAG + ".dlx";

    /// 发送消息,用这个队列
    public static final String SEND_QUEUE = "business.topic." + FLAG + ".dlq";

    /// 延迟消息处理路由
    public static final String ROUTING_KEY = "business.topic." + FLAG + ".routing.key";

    /// 接收消息,用这个队列
    public static final String RECEIVE_QUEUE = "business.topic." + FLAG + ".process.queue";

    @Bean("businessTopicDlx" + FLAG)
    public DirectExchange businessTopicDlx() {
        return new DirectExchange(DLX, true, false);
    }

    @Bean("businessTopicDlq" + FLAG)
    public Queue businessTopicDlq() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", DLX);
        args.put("x-dead-letter-routing-key", ROUTING_KEY);
        // 不要在这里设 x-message-ttl，由消息动态指定
        return QueueBuilder.durable(SEND_QUEUE).withArguments(args).build();
    }

    @Bean("businessTopicProcessQueue" + FLAG)
    public Queue businessTopicProcessQueue() {
        return QueueBuilder.durable(RECEIVE_QUEUE).build();
    }

    @Bean("businessTopicBinding" + FLAG)
    public Binding businessTopicBinding() {
        return BindingBuilder.bind(businessTopicProcessQueue())
                .to(businessTopicDlx())
                .with(ROUTING_KEY);
    }
}
