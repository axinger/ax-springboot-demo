package com.github.axinger.mqtt;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class Mqtt5Route extends RouteBuilder {


    @Override
    public void configure() throws Exception {
        from("paho-mqtt5:testTopic?qos=1")
                .log("MQTT连接建立成功，开始监听testTopic主题")
                .log("Received MQTT message: ${body}")
                .process(exchange -> {
                    String message = exchange.getIn().getBody(String.class);
                    System.out.println("从MQTT订阅消息 Processing message: " + message);
                    // 打印消息头信息
                    System.out.println("消息头: " + exchange.getIn().getHeaders());
                })
                .onException(Exception.class)
                .log("MQTT消息处理异常: ${exception.message}")
                .handled(true)
                .end();

//        from("paho-mqtt5:testTopic2?qos=1")
//                .log("MQTT连接建立成功，开始监听testTopic主题")
//                .log("Received MQTT message: ${body}")
//                // 添加幂等消费者
////                .idempotentConsumer(header("messageId"), MemoryIdempotentRepository.memoryIdempotentRepository(200))
//                .process(exchange -> {
//                    String message = exchange.getIn().getBody(String.class);
//                    System.out.println("从MQTT订阅消息 Processing message: " + message);
//                    // 打印消息头信息
//                    System.out.println("消息头: " + exchange.getIn().getHeaders());
//                })
//                .onException(Exception.class)
//                .log("MQTT消息处理异常: ${exception.message}")
//                .handled(true)
//                .end();
    }
}
