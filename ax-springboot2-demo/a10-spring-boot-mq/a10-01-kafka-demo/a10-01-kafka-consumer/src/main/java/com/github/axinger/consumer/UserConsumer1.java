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

    // 注意：kafkaListenerContainerFactory 是一个支持手动确认和异步提交偏移量的自定义工厂。
    // 如果您使用的是默认工厂，则将自动确认和同步提交偏移量。
    @KafkaListener(
//            id = "ax_kafka_demo1",
            topics = Topic.USER_DTO)
    public void listen(
//            GenericMessage<String> genericMessage,
//            @Payload String data,
            ConsumerRecord<?, MessageUserDTO> consumerRecord,
            Consumer<?, ?> consumer,
            Acknowledgment ack) {
        try {

//            {
//                System.out.println("@Payload 读取数据=============");
//                log.info("@Payload={}", data);
//            }

            {
                System.out.println("ConsumerRecord 读取数据=============");
                // 获取主题名称
                String topic = consumerRecord.topic();
                Headers headers = consumerRecord.headers();
//                for (Header header : headers) {
//                    String  headerKey = header.key();
//                    String headerValue = new String(header.value());
//                    log.info("headerKey={},headerValue={}", headerKey, headerValue);
//                }

                List<Map<String, Object>> myHeaders = StreamSupport.stream(headers.spliterator(), false)
                        .map(header -> {
                            Map<String, Object> map = new HashMap<>();
                            map.put("key", header.key());
                            map.put("value", new String(header.value(), StandardCharsets.UTF_8));
                            return map;
                        })
                        .toList();

                // 获取主题分区 ID
                int partition = consumerRecord.partition();
                // 获取偏移量
                long offset = consumerRecord.offset();
                Object key = consumerRecord.key();
                MessageUserDTO value = consumerRecord.value();

                log.info("topic={},headers={},partition={},offset={},key={},value={}", topic, myHeaders, partition, offset, key, value);
            }

//            {
//                System.out.println("GenericMessage 读取数据=============");
//                MessageHeaders headers = genericMessage.getHeaders();
//                String payload = genericMessage.getPayload();
//                log.info("headers={}，data={}", headers, payload);
//            }


            {
                System.out.println("Consumer 只有提交功能=============");
                consumer.commitAsync((offsets, exception) -> {
                    if (exception != null) {
                        log.error("提交偏移量失败 {}", exception.getMessage(), exception);
                    } else {
                        log.info("提交偏移量成功，offsets={}", offsets);
                    }
                });
            }

            ack.acknowledge();

        } catch (Exception e) {
            log.error("Failed to process message: {} ", e.getMessage());
            // 处理失败的情况，可以根据具体需求重试或将其发送到 DLQ 等操作
        }
    }

}
