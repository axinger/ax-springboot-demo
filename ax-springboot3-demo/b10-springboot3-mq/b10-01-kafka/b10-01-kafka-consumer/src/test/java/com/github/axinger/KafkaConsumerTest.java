//package com.github.axinger;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.test.EmbeddedKafkaBroker;
//import org.springframework.kafka.test.context.EmbeddedKafka;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//@SpringBootTest
//@EmbeddedKafka(topics = "testTopic", partitions = 1)
//class KafkaConsumerTest {
//
//    @Autowired
//    private EmbeddedKafkaBroker  embeddedKafkaBroker;
//
//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;
//
//    @Test
//    void testConsumer() throws Exception {
//        // 发送测试消息
//        kafkaTemplate.send("testTopic", "testKey", "testValue");
//
//        // 等待消费者处理
//        Thread.sleep(1000);
//
//        // 这里假设你的消费者会更新某个状态或存储接收的消息
//        // 然后验证这个状态或存储
//        assertThat(MyMessageStorage.getLastMessage()).isEqualTo("testValue");
//    }
//}
