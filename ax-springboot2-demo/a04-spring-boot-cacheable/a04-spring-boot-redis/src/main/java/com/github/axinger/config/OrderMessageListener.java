package com.github.axinger.config;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class OrderMessageListener implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 处理接收到的消息
//        String receivedMessage = new String(message.getBody(), StandardCharsets.UTF_8);

        String channel = new String(message.getChannel());
        String body = new String(message.getBody());
        System.out.println("Received message: " + body + " from channel: " + channel);

    }
}
