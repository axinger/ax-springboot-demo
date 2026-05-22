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

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;


@Component
@Slf4j
public class UserConsumer1 {

    /**
     * 消费 USER_DTO 主题消息
     * 注意：使用手动确认模式，需要显式调用 ack.acknowledge()
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

            // 解析 Headers
            Headers headers = consumerRecord.headers();
            List<Map<String, Object>> headerList = StreamSupport.stream(headers.spliterator(), false)
                    .map(header -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("key", header.key());
                        map.put("value", new String(header.value(), StandardCharsets.UTF_8));
                        return map;
                    })
                    .toList();

            log.info("收到消息 - topic={}, partition={}, offset={}, key={}, headers={}, value={}",
                    topic, partition, offset, key, headerList, value);

            // TODO: 在此处添加业务处理逻辑
            processMessage(value);

            // 异步提交偏移量
            consumer.commitAsync((offsets, exception) -> {
                if (exception != null) {
                    log.error("异步提交偏移量失败, offsets={}, error={}", offsets, exception.getMessage(), exception);
                } else {
                    log.debug("异步提交偏移量成功, offsets={}", offsets);
                }
            });

            // 手动确认消息
            ack.acknowledge();

        } catch (Exception e) {
            log.error("处理消息失败, error={}", e.getMessage(), e);
            // 根据业务需求决定是否需要重试或发送到死信队列
        }
    }

    /**
     * 处理消息的业务逻辑
     *
     * @param user 用户消息对象
     */
    private void processMessage(MessageUserDTO user) {
        // 模拟业务处理
        log.info("处理用户消息: guild={}, name={}, age={}",
                user.getGuild(), user.getName(), user.getAge());
    }

}
