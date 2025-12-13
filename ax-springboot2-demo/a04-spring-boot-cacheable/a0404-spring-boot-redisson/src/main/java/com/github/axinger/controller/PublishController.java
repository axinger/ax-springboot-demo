package com.github.axinger.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.redisson.api.RPatternTopic;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * 订阅发布
 */
@Tag(name = "订阅发布2")
@RestController
public class PublishController {

    @Autowired
    private RedissonClient redissonClient;


    @PostConstruct
    public void init() {
//        RPatternTopic patternTopic = redissonClient.getPatternTopic("news.*");
        RPatternTopic patternTopic = redissonClient.getPatternTopic("ax-test:*");
//        RPatternTopic patternTopic = redissonClient.getPatternTopic(channelName);
        patternTopic.addListener(String.class, (pattern, channel, msg) -> {
            System.out.println("模式匹配: " + pattern + " 频道: " + channel + " 消息: " + msg);
        });
    }


    @GetMapping("/publish")
    public void publishMessage(String message) {
        String channelName = "ax-test:myChannel";
        RTopic topic = redissonClient.getTopic(channelName);
        long clientsReceived = topic.publish(message);
        System.out.println("消息已发布，接收客户端数量: " + clientsReceived);
    }

    @GetMapping("/publish2")
    public void publish2(String message) {
        String channelName2 = "ax-test:myChannel2";
        RTopic topic = redissonClient.getTopic(channelName2);
        long clientsReceived = topic.publish(message);
        System.out.println("消息已发布，接收客户端数量: " + clientsReceived);
    }


}
