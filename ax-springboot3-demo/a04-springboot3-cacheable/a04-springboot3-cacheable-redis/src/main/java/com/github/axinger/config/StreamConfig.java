package com.github.axinger.config;


import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

/**
 * @author xiaobo
 */
@Configuration
@Slf4j
@Import(RedisConfig.class)
public class StreamConfig {

    @Autowired
    private StreamMessageListenerContainer<String, ObjectRecord<String, String>> streamMessageListenerContainer;


    @PostConstruct
    public void init() {
        MessageConsumer messageConsumer = new MessageConsumer();

        // 方法配置了容器来接收来自特定消费者组和消费者名称的消息。它还指定了要读取消息的起始偏移量，以确定从哪里开始读取消息。
        streamMessageListenerContainer.receive(
                Consumer.from("your-consumer-group", "your-consumer-name"),
                StreamOffset.create("your-stream-name", ReadOffset.lastConsumed()),
                messageConsumer);


        //指定消费最新的消息
//        StreamOffset<String> offset = StreamOffset.create("your-stream-name", ReadOffset.lastConsumed());
//
//        //创建消费者 指定消费者组和消费者名字（注意，这里使用到用户组时，发送消息时必须有用户组，不然会报错，消息消费不成功）
//        Consumer consumer = Consumer.from("your-consumer-group", "your-consumer-name");
//
//        StreamMessageListenerContainer.StreamReadRequest<String> streamReadRequest = StreamMessageListenerContainer.StreamReadRequest.builder(offset)
//                .errorHandler((error) -> log.error(error.getMessage()))
//                .cancelOnError(e -> false)
//                .consumer(consumer)
//                //关闭自动ack确认
//                .autoAcknowledge(false)
//                .build();
//        //指定消费者对象
//        streamMessageListenerContainer.register(streamReadRequest, messageConsumer);


        // 独立消费
        String streamKey = Cosntants.STREAM_KEY_001;
        streamMessageListenerContainer.receive(StreamOffset.fromStart(streamKey),
                new AsyncConsumeStreamListener("独立消费", null, null));

        // 消费组A,不自动ack
        // 从消费组中没有分配给消费者的消息开始消费
        streamMessageListenerContainer.receive(Consumer.from("group-a", "consumer-a"),
                StreamOffset.create(streamKey, ReadOffset.lastConsumed()), new AsyncConsumeStreamListener("消费组消费", "group-a", "consumer-a"));
        // 从消费组中没有分配给消费者的消息开始消费
        streamMessageListenerContainer.receive(Consumer.from("group-a", "consumer-b"),
                StreamOffset.create(streamKey, ReadOffset.lastConsumed()), new AsyncConsumeStreamListener("消费组消费A", "group-a", "consumer-b"));

        // 消费组B,自动ack
        streamMessageListenerContainer.receiveAutoAck(Consumer.from("group-b", "consumer-a"),
                StreamOffset.create(streamKey, ReadOffset.lastConsumed()), new AsyncConsumeStreamListener("消费组消费B", "group-b", "consumer-bb"));

        // 如果需要对某个消费者进行个性化配置在调用register方法的时候传递`StreamReadRequest`对象


        // 方法启动了消息监听容器，使其开始监听消息。一旦容器被启动，它将开始接收并处理来自Redis流的消息。
        streamMessageListenerContainer.start();

    }
}

