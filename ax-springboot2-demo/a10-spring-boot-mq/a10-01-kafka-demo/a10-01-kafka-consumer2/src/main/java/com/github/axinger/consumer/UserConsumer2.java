//package com.github.axinger.consumer;
//
//import com.github.axinger.api.Topic;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.consumer.Consumer;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.support.Acknowledgment;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.stereotype.Component;
//
//
//@Component
//@Slf4j
//public class UserConsumer2 {
//
//    // 注意：kafkaListenerContainerFactory 是一个支持手动确认和异步提交偏移量的自定义工厂。
//    // 如果您使用的是默认工厂，则将自动确认和同步提交偏移量。
//    @KafkaListener(id = "ax_kafka_demo1",
//            topics = Topic.USER)
//    public void listen(ConsumerRecord<String, String> record,
//                       @Payload String data,
//                       Acknowledgment ack,
//                       Consumer<?, ?> consumer) {
//        try {
//            log.info("接收到消息==================================================");
//            // 模拟业务处理
//            // handleBusiness(record);
//
//            // 获取主题名称
//            String topic = record.topic();
//
//            // 获取主题分区 ID
//            int partition = record.partition();
//
//            // 获取偏移量
//            long offset = record.offset();
//
//            // 获取键和值
//            String key = record.key();
//            String value = record.value();
//            log.info("Received message. Topic: {}, Partition: {}, Offset: {}, Key: {}, Value: {}", topic, partition, offset, key, value);
//            log.info("解析data = {}", data);
//
//            // 手动确认消息状态，并异步提交最新的消息偏移量。
//            ack.acknowledge();
//            // 不提交偏移量
//            // consumer.commitAsync((offsets, exception) -> {
//            //     if (exception != null) {
//            //         log.error("提交偏移量失败 {}", exception.getMessage(), exception);
//            //     } else {
//            //         log.info("提交偏移量成功 {}", offsets.keySet());
//            //     }
//            // });
//        } catch (Exception e) {
//            log.error("Failed to process message: {} ", e.getMessage());
//            // 处理失败的情况，可以根据具体需求重试或将其发送到 DLQ 等操作
//        }
//    }
//
//}
