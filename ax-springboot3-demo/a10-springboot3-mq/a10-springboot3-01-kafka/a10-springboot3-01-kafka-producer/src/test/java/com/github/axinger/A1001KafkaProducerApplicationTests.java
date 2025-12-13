package com.github.axinger;

import com.github.axinger.api.Topic;
import com.github.axinger.api.model.MessageUserDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.CountDownLatch;

/**
 * Kafka生产者测试类
 * 测试Kafka消息发送功能
 */
@Slf4j
@SpringBootTest
class A1001KafkaProducerApplicationTests {

    @Autowired
    private KafkaTemplate<String, MessageUserDTO> kafkaTemplate;

    @Autowired
    private KafkaTemplate<String, String> stringKafkaTemplate;

    /**
     * 测试发送UserDTO对象消息
     * 验证Kafka消息发送成功和失败的回调处理
     */
    @SneakyThrows
    @Test
    void test1() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        MessageUserDTO user = new MessageUserDTO();

//        ListenableFuture<SendResult<String, UserDTO>> send = kafkaTemplate.send(Topic.USER_DTO, user);

        // 创建ProducerRecord并添加头信息
        ProducerRecord<String, MessageUserDTO> record = new ProducerRecord<>(Topic.USER_DTO, user);
        record.headers().add("target-consumer", "group1".getBytes()); // 指定发送给group1

        ListenableFuture<SendResult<String, MessageUserDTO>> send = kafkaTemplate.send(record);

        send.addCallback(
                sendResult -> {
                    log.info("发送消息成功===================");
                    RecordMetadata recordMetadata = sendResult.getRecordMetadata();
                    log.info("recordMetadata = {}", recordMetadata);
                    MessageUserDTO value = sendResult.getProducerRecord().value();
                    log.info("value = {}", value);
                    countDownLatch.countDown();
                },
                e -> {
                    log.error("发送消息失败: {}", e.getMessage(), e);
                    countDownLatch.countDown();
                }
        );

        countDownLatch.await();
    }

    /**
     * 测试发送字符串消息
     * 验证Kafka字符串消息发送及元数据获取
     */
    @SneakyThrows
    @Test
    void sendStringMessage() {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        String message = "测试消息内容: " + System.currentTimeMillis();

        ListenableFuture<SendResult<String, String>> send = stringKafkaTemplate.send(Topic.TEST_01, message);

        send.addCallback(
                sendResult -> {
                    log.info("发送字符串消息成功===================");
                    RecordMetadata recordMetadata = sendResult.getRecordMetadata();
                    log.info("消息发送到主题: {}, 分区: {}, 偏移量: {}",
                            recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
                    countDownLatch.countDown();
                },
                e -> {
                    log.error("发送字符串消息失败: {}", e.getMessage(), e);
                    countDownLatch.countDown();
                }
        );

        countDownLatch.await();
    }
}
