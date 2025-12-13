package com.github.axinger.config;

import org.apache.pulsar.client.api.ConsumerBuilder;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.SubscriptionType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PulsarConfig {

    @Bean
    public ConsumerBuilder<?> customConsumerBuilder(PulsarClient pulsarClient) {
        return pulsarClient.newConsumer()
//                .subscriptionName("test01")
                .subscriptionType(SubscriptionType.Shared)
                .receiverQueueSize(10); // 每个消费者可以缓冲的消息数
    }
}
