//package com.github.axinger.consumer;
//
//import com.github.axinger.api.Topic;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.support.Acknowledgment;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//public class UserConsumer3 {
//
//    /**
//     * 这个消费者属于默认的消费者组 (my_group_id2)
//     * 它会独立消费 Topic.TEST_01 中的消息，与 UserConsumer1 形成竞争关系
//     * 但是由于属于同一消费者组，对于每个分区，只有一个消费者实例会消费消息
//     */
//    @KafkaListener(topics = Topic.TEST_01)
//    public void listenSameGroup(ConsumerRecord<String, String> record, Acknowledgment ack) {
//        log.info("同组消费者接收到消息: topic={}, partition={}, offset={}, key={}, value={}",
//                record.topic(), record.partition(), record.offset(), record.key(), record.value());
//        // 模拟业务处理
//        processMessage(record.value());
//        ack.acknowledge();
//    }
//
//    /**
//     * 这个消费者属于不同的消费者组 (my_group_id3)
//     * 它会独立消费 Topic.TEST_01 中的所有消息，与 my_group_id1 和 my_group_id2 组互不影响
//     */
//    @KafkaListener(groupId = "my_group_id3", topics = Topic.TEST_01)
//    public void listenDifferentGroup(ConsumerRecord<String, String> record, Acknowledgment ack) {
//        log.info("不同组消费者接收到消息: topic={}, partition={}, offset={}, key={}, value={}",
//                record.topic(), record.partition(), record.offset(), record.key(), record.value());
//        // 模拟业务处理
//        processMessage(record.value());
//        ack.acknowledge();
//    }
//
//    private void processMessage(String message) {
//        // 模拟业务处理耗时
//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//        log.info("处理消息完成: {}", message);
//    }
//}
