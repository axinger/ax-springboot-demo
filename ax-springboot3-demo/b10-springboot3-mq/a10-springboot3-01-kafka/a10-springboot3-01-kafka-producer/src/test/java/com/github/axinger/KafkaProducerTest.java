//package com.github.axinger;
//
//import org.apache.kafka.clients.consumer.Consumer;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.test.EmbeddedKafkaBroker;
//import org.springframework.kafka.test.context.EmbeddedKafka;
//import org.springframework.kafka.test.utils.KafkaTestUtils;
//
//import java.util.Map;
//
//@SpringBootTest
//@EmbeddedKafka(topics = {"testTopic"}, partitions = 1, brokerProperties = {
//    "listeners=PLAINTEXT://localhost:9092", "port=9092"
//})
//class KafkaProducerTest {
//
//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;
//
//    @Autowired
//    private EmbeddedKafkaBroker embeddedKafkaBroker;
//
//    @Test
//    void testSend() throws Exception {
//        // 准备测试消费者
//        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafkaBroker);
//        Consumer<String, String> consumer = new DefaultKafkaConsumerFactory<String, String>(consumerProps)
//            .createConsumer();
//        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, "testTopic");
//
//        // 发送消息
//        kafkaTemplate.send("testTopic", "testKey", "testValue");
//
//        // 验证消息
//        ConsumerRecord<String, String> record = KafkaTestUtils.getSingleRecord(consumer, "testTopic");
//        assertThat(record.key()).isEqualTo("testKey");
//        assertThat(record.value()).isEqualTo("testValue");
//
//        consumer.close();
//    }
//}
