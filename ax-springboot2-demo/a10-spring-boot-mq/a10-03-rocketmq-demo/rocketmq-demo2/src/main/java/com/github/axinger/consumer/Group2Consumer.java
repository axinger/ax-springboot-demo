package com.github.axinger.consumer;

import com.github.axinger.config.Topic;
import com.github.axinger.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class Group2Consumer {


    @Component
    @RocketMQMessageListener(
            consumerGroup = Topic.GROUP_2 + "a",
            topic = Topic.TOPIC_2,
            selectorType = SelectorType.TAG,
            selectorExpression = Topic.Tag_3,
            consumeThreadNumber = 4
    )
    public static class ConsumerGROUP_2Tag_3 implements RocketMQListener<User> {
        @Override
        public void onMessage(User user) {
            log.info("收到消息GROUP_2:Tag_3当前线程池={},user={}", Thread.currentThread().getName(), user.getId());
        }
    }
}
