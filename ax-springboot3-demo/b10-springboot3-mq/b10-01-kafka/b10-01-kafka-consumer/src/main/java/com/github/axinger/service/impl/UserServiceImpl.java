package com.github.axinger.service.impl;

import com.github.axinger.api.Topic;
import com.github.axinger.api.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * Kafka 消息服务实现
 * 注意：此服务使用原生 KafkaConsumer API，与 @KafkaListener 方式不同
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    /**
     * 从 Kafka 主题中获取消息（阻塞式）
     * 注意：此方法每次调用都会创建新的消费者实例，生产环境建议使用连接池或复用消费者
     *
     * @return 消息内容
     */
    @Override
    public String getMsg() {
        Properties props = new Properties();
        // TODO: 应该从配置文件读取，避免硬编码
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "group-service");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "earliest");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        try (KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(props)) {
            kafkaConsumer.subscribe(List.of(Topic.SIMPLE));
            
            log.info("开始轮询消息...");
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(1));
            
            for (ConsumerRecord<String, String> record : records) {
                log.info("获取消息: topic={}, partition={}, offset={}, value={}",
                        record.topic(), record.partition(), record.offset(), record.value());
                return record.value();
            }
            
            log.info("未获取到消息");
            return null;
            
        } catch (Exception e) {
            log.error("消费消息失败", e);
            return null;
        }
    }

    /**
     * 从内存队列中获取消息（已废弃，保留接口兼容性）
     *
     * @return null
     */
    @Override
    @Deprecated
    public synchronized String getMsg2() {
        log.warn("getMsg2 方法已废弃，请使用 getMsg()");
        return null;
    }

    /**
     * 标记消息为已处理（已废弃，保留接口兼容性）
     *
     * @param fuBh 服务编号
     */
    @Override
    @Deprecated
    public void resolve(Long fuBh) {
        log.warn("resolve 方法已废弃");
    }

}
