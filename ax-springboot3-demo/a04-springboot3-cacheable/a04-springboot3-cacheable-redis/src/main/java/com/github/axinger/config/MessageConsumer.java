package com.github.axinger.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Service;

/**
 * @author xiaobo
 */
@Service
public class MessageConsumer implements StreamListener<String, ObjectRecord<String, String>> {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;


    @Override
    public void onMessage(ObjectRecord<String, String> message) {
        String stream = message.getStream();
        String messageId = message.getId().toString();
        String messageBody = message.getValue();

        System.out.println("Received message from Stream '" + stream + "' with messageId: " + messageId);
        System.out.println("Message body: " + messageBody);


        // 通过RedisTemplate手动确认消息，确认之后消息会从队列中消失，如果不确认，可能存在重复消费
        Long acknowledge = redisTemplate.opsForStream().acknowledge("group-1", message);
        if (acknowledge > 0) {
            System.out.println("acknowledge的值是：" + acknowledge);
        }

    }
}

