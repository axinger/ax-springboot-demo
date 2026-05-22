package com.github.axinger.consumer;

import com.github.axinger.api.Topic;
import com.github.axinger.api.model.MessageUserDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class UserConsumer1 {

    /**
     * 消费 USER_DTO 主题消息（consumer2 - group2）
     * 注意：使用手动确认模式
     *
     * @param consumerRecord Kafka 消费者记录
     * @param consumer       Kafka 消费者实例
     * @param ack            手动确认对象
     */
    @KafkaListener(topics = Topic.USER_DTO)
    public void listen(
            ConsumerRecord<?, MessageUserDTO> consumerRecord,
            Consumer<?, ?> consumer,
            Acknowledgment ack) {
        try {
            // 获取消息基本信息
            String topic = consumerRecord.topic();
            int partition = consumerRecord.partition();
            long offset = consumerRecord.offset();
            Object key = consumerRecord.key();
            MessageUserDTO value = consumerRecord.value();

            log.info("[Consumer2-Group2] 收到消息 - topic={}, partition={}, offset={}, key={}, value={}",
                    topic, partition, offset, key, value);

            // TODO: 在此处添加业务处理逻辑
            processMessage(value);

            // 异步提交偏移量
            consumer.commitAsync((offsets, exception) -> {
                if (exception != null) {
                    log.error("[Consumer2-Group2] 异步提交偏移量失败, error={}", exception.getMessage(), exception);
                } else {
                    log.debug("[Consumer2-Group2] 异步提交偏移量成功");
                }
            });

            // 手动确认消息
            ack.acknowledge();

        } catch (Exception e) {
            log.error("[Consumer2-Group2] 处理消息失败, error={}", e.getMessage(), e);
        }
    }

    /**
     * 处理消息的业务逻辑
     *
     * @param user 用户消息对象
     */
    private void processMessage(MessageUserDTO user) {
        log.info("[Consumer2-Group2] 处理用户消息: name={}, age={}",
                user.getName(), user.getAge());
    }

}
