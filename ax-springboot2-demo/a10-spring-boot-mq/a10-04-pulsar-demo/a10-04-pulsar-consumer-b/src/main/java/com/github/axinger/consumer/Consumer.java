package com.github.axinger.consumer;


import com.github.axinger.topic.Topic;
import io.github.majusko.pulsar.annotation.PulsarConsumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.SubscriptionType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Component
public class Consumer {

    @PulsarConsumer(topic = Topic.DELIVER_AFTER_TOPIC,
            subscriptionType = SubscriptionType.Shared,// 延迟消息必须是共享模式
            clazz = Map.class)
    public void deliverAfterTopic(Map message) {
        log.info("接收到消息 time = {},  mes = {}", LocalDateTime.now(), message);
    }

    /**
     * Pulsar的消费模型有4种：
     */

    /**
     * 独占模式(Exclusive):同一个topic只能有一个消费者订阅，如果多个消费者订阅，就会出错。
     * 但是 一个消费者, 同一个主题,可以监听多次
     *
     * @param message
     */
    //@PulsarConsumer(topic = Topic.EXCLUSIVE_TOPIC,
    //        subscriptionType = SubscriptionType.Exclusive,
    //        clazz = Map.class)
    // public void Exclusive1(Map message) {
    //    log.info("Exclusive1 time = {},  mes = {}", LocalDateTime.now(), message);
    //}
    //
    //@PulsarConsumer(topic = Topic.EXCLUSIVE_TOPIC,
    //        subscriptionType = SubscriptionType.Exclusive,
    //        clazz = Map.class)
    // public void Exclusive2(Map message) {
    //    log.info("Exclusive2 time = {}, mes = {}", LocalDateTime.now(), message);
    //}


    /**
     * 灾备模式(Failover)：同一个topic可以有多个消费者订阅，但是只能有一个消费者消费，
     * 其他订阅的消费者作为故障转移的消费者，只有当前消费者出了故障才可以进行消费当前的topic
     * 一个消费者, 同一个主题,可以监听多次
     *
     * @param message
     */
    @PulsarConsumer(topic = Topic.FAILOVER_TOPIC,
            subscriptionType = SubscriptionType.Failover,
            clazz = Map.class)
    public void Failover1(Map message) {
        log.info("Failover1 time = {},  mes = {}", LocalDateTime.now(), message);
    }

    @PulsarConsumer(topic = Topic.FAILOVER_TOPIC,
            subscriptionType = SubscriptionType.Failover,
            clazz = Map.class)
    public void Failover2(Map message) {
        log.info("Exclusive2 time = {},  mes = {}", LocalDateTime.now(), message);
    }

    /**
     * 共享订阅(Shared):同一个topic可以由多个消费者订阅和消费。消息通过round robin轮询机制分发给不同的消费者，
     * 并且每个消息仅会被分发给一个消费者。当消费者断开，发送给它的没有被消费的消息还会被重新分发给其它存活的消费者
     * <p>
     * 一个消费者, 同一个主题,可以监听多次, 比如 A订阅2次, B订阅2次, 总共4个进行分配 2次, 消费者,在自己分配
     */

    @PulsarConsumer(topic = Topic.SHARED_TOPIC,
            subscriptionType = SubscriptionType.Shared,
            clazz = Map.class)
    public void Shared1(Map message) {
        log.info("Shared1 time = {},  mes = {}", LocalDateTime.now(), message);
    }

    //@PulsarConsumer(topic = Topic.SHARED_TOPIC,
    //        subscriptionType = SubscriptionType.Failover,
    //        clazz = Map.class)
    // public void Shared2(Map message) {
    //    log.info("Shared2 time = {},  mes = {}", LocalDateTime.now(), message);
    //}

    /**
     * Key_Shared：消息和消费者都会绑定一个key，消息只会发送给绑定同一个key的消费者。
     * 如果有新消费者建立连接或者有消费者断开连接，就需要更新一些消息的key
     */
    @PulsarConsumer(topic = Topic.KEY_SHARED_TOPIC,
            subscriptionType = SubscriptionType.Key_Shared,
            consumerName = "2",
            clazz = Map.class)
    public void Key_Shared1(Map message) {
        log.info("Shared1 time = {},  mes = {}", LocalDateTime.now(), message);
    }
}
