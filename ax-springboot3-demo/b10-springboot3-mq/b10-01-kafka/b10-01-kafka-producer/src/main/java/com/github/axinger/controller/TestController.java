package com.github.axinger.controller;

import cn.hutool.core.util.IdUtil;
import com.github.axinger.api.Topic;
import com.github.axinger.api.model.MessageUserDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class TestController {

    @Autowired
    private KafkaTemplate<String, MessageUserDTO> kafkaTemplate;

    /**
     * 发送带 Header 的消息到指定消费者组（同步等待结果）
     *
     * @param groupId 目标消费者组ID
     * @return 发送结果
     */
    @GetMapping("/test1-sync")
    String test1Sync(String groupId) {
        MessageUserDTO user = new MessageUserDTO();
        user.setGuild(IdUtil.fastSimpleUUID());
        user.setName("jim");
        user.setCurrentTime(new Date());

        // 创建ProducerRecord并添加头信息
        ProducerRecord<String, MessageUserDTO> record = new ProducerRecord<>(Topic.USER_DTO, user);
        record.headers().add("target-consumer", groupId.getBytes());

        try {
            // 同步发送，等待结果（最多等待10秒）
            SendResult<String, MessageUserDTO> result = kafkaTemplate.send(record)
                    .get(10, TimeUnit.SECONDS);

            RecordMetadata metadata = result.getRecordMetadata();
            log.info("消息发送成功 - topic={}, partition={}, offset={}",
                    metadata.topic(), metadata.partition(), metadata.offset());

            return String.format("发送成功: topic=%s, partition=%d, offset=%d",
                    metadata.topic(), metadata.partition(), metadata.offset());

        } catch (Exception e) {
            log.error("消息发送失败: {}", e.getMessage(), e);
            // TODO: 这里可以执行自定义业务逻辑，如：
            // 1. 保存到数据库，稍后重试
            // 2. 发送到死信队列
            // 3. 记录告警
            return "发送失败: " + e.getMessage();
        }
    }

    /**
     * 发送带 Header 的消息（异步回调方式）
     *
     * @param groupId 目标消费者组ID
     */
    @SneakyThrows
    @GetMapping("/test1")
    void test1(String groupId) {
        MessageUserDTO user = new MessageUserDTO();
        user.setGuild(IdUtil.fastSimpleUUID());
        user.setName("jim");
        user.setCurrentTime(new Date());

        // 创建ProducerRecord并添加头信息
        ProducerRecord<String, MessageUserDTO> record = new ProducerRecord<>(Topic.USER_DTO, user);
        record.headers().add("target-consumer", groupId.getBytes()); // 指定发送给特定消费者组

        CompletableFuture<SendResult<String, MessageUserDTO>> send = kafkaTemplate.send(record);

        // 异步回调处理发送结果
        send.whenComplete((result, ex) -> {
            if (ex == null) {
                RecordMetadata recordMetadata = result.getRecordMetadata();
                log.info("消息发送成功 - topic={}, partition={}, offset={}",
                        recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
                log.info("消息内容: {}", result.getProducerRecord().value());
            } else {
                log.error("消息发送失败: {}", ex.getMessage(), ex);

                // TODO: 失败处理策略（根据业务需求选择）
                handleSendFailure(user, ex);
            }
        });
    }

    /**
     * 处理消息发送失败的业务逻辑
     *
     * @param user      失败的消息对象
     * @param exception 异常信息
     */
    private void handleSendFailure(MessageUserDTO user, Throwable exception) {
        // 方案1: 记录到数据库，定时任务重试
        // messageFailRepository.save(new MessageFailRecord(user, exception.getMessage()));

        // 方案2: 发送到死信 Topic
        // kafkaTemplate.send("dead-letter-topic", user);

        // 方案3: 记录日志并告警
        log.error("消息发送失败，需要人工干预 - user={}, error={}", user, exception.getMessage());

        // 方案4: 使用本地消息表（推荐用于重要业务）
        // localMessageTableService.savePendingMessage(user);
    }

    /**
     * 发送普通JSON消息（异步方式）
     */
    @SneakyThrows
    @GetMapping("/test2")
    void test2() {
        MessageUserDTO user = new MessageUserDTO();
        user.setGuild(IdUtil.fastSimpleUUID());
        user.setName("tom");
        user.setAge(25);
        user.setCurrentTime(new Date());

        CompletableFuture<SendResult<String, MessageUserDTO>> sent = kafkaTemplate.send(Topic.USER_JSON, user);

        // 异步回调处理
        sent.whenComplete((result, ex) -> {
            if (ex == null) {
                RecordMetadata recordMetadata = result.getRecordMetadata();
                log.info("消息发送成功 - topic={}, partition={}, offset={}",
                        recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
            } else {
                log.error("消息发送失败: {}", ex.getMessage(), ex);
                handleSendFailure(user, ex);
            }
        });
    }

    /**
     * 批量发送消息示例
     */
    @GetMapping("/test-batch")
    String testBatch() {
        int count = 10;
        for (int i = 0; i < count; i++) {
            MessageUserDTO user = new MessageUserDTO();
            user.setGuild(IdUtil.fastSimpleUUID());
            user.setName("user-" + i);
            user.setAge(20 + i);
            user.setCurrentTime(new Date());

            int finalI = i;
            kafkaTemplate.send(Topic.USER_JSON, user)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("批量发送中第{}条消息失败", finalI, ex);
                            handleSendFailure(user, ex);
                        }
                    });
        }

        return String.format("已提交%d条消息发送请求", count);
    }


}
