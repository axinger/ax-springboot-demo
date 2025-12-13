package com.github.axinger.consumer;


import cn.hutool.core.util.RandomUtil;
import com.github.axinger.model.Person;
import com.github.axinger.topic.Topic;
import io.github.majusko.pulsar.annotation.PulsarConsumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.SubscriptionType;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class Consumer {

    /**
     * 共享订阅(Shared):同一个topic可以由多个消费者订阅和消费。消息通过round robin轮询机制分发给不同的消费者，
     * 并且每个消息仅会被分发给一个消费者。当消费者断开，发送给它的没有被消费的消息还会被重新分发给其它存活的消费者
     * <p>
     * 一个消费者, 同一个主题,可以监听多次, 比如 A订阅2次, B订阅2次, 总共4个进行分配 2次, 消费者,在自己分配
     */

    @PulsarConsumer(topic = Topic.SHARED_TOPIC,
            subscriptionType = SubscriptionType.Shared,
//            subscriptionName = "test01",
            clazz = Person.class)
    public void shared1(Person person) throws InterruptedException {

        int randomInt = RandomUtil.randomInt(100, 5000);
        log.info("GROUP_2:当前线程池={},监听到消息：user={},randomInt={}", Thread.currentThread().getName(), person.getId(), randomInt);
        TimeUnit.MILLISECONDS.sleep(randomInt);
    }

}
