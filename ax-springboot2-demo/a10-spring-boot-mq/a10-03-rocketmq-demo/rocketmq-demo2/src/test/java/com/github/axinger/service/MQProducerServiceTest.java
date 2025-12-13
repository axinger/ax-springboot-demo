package com.github.axinger.service;

import com.github.axinger.config.Topic;
import com.github.axinger.model.User;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MQProducerServiceTest {

    @Autowired
    private MQProducerService mqProducerService;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Test
    void send() {
        User user = new User();
        user.setName("jim");
        user.setAge(10);
        rocketMQTemplate.convertAndSend(Topic.TOPIC_1 + ":" + Topic.Tag_1, user);
    }

    @Test
    void sendMsg() {
        User user = new User();
        user.setName("jim");
        user.setAge(10);
        mqProducerService.sendMsg(user);
    }

    @Test
    void sendAsyncMsg() {
        User user = new User();
        user.setName("jim");
        user.setAge(10);
        mqProducerService.sendAsyncMsg(user);
    }

    @Test
    void sendDelayMsg() {
        User user = new User();
        user.setName("延迟消息");
        user.setAge(10);
        mqProducerService.sendDelayMsg(user, 5);
    }

    @Test
    void sendOneWayMsg() {
        User user = new User();
        user.setName("jim");
        user.setAge(10);
        mqProducerService.sendOneWayMsg(user);
    }

    @Test
    void sendTagMsg() {
        User user = new User();
        user.setName("jim");
        user.setAge(10);
        mqProducerService.sendTagMsg(user);
    }
}
