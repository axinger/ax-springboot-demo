package com.github.axinger.config;

import com.github.axinger.model.RedisKeys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;


@Component
public class RedisSubscriber {

    @Autowired
    private RedisMessageListenerContainer redisMessageListenerContainer;

    @Autowired
    private OrderMessageListener orderMessageListener;

    @PostConstruct
    public void subscribe() {
        // 订阅特定的频道
        redisMessageListenerContainer.addMessageListener(orderMessageListener, new ChannelTopic(RedisKeys.PLACE_ORDER_KEY));
        //  redisContainer.removeMessageListener(listener); // 停止订阅
    }
}
