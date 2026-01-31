package com.github.axinger.consumer;

import com.github.axinger.config.Topic;
import com.github.axinger.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQReplyListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RocketMQMessageListener(
        consumerGroup = Topic.GROUP_3,
        topic = Topic.TOPIC_3,
        maxReconsumeTimes = 3,
        consumeTimeout = 10L
)
public class Group3Consumer implements RocketMQReplyListener<User, String> {
    @Override
    public String onMessage(User user) {
        log.info("GROUP_3:当前线程池={},监听到消息：user={}", Thread.currentThread().getName(), user);
        return "成功";
    }
}
