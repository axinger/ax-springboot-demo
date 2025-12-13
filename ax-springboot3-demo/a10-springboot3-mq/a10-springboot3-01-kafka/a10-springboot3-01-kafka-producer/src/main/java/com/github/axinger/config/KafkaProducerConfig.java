//package com.github.axinger.config;
//
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.core.ProducerFactory;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//public class KafkaProducerConfig {
//
//    @Bean
//    public ProducerFactory<String, String> stringProducerFactory() {
//        Map<String, Object> configProps = new HashMap<>();
//        configProps.put("bootstrap.servers", "192.168.101.134:9092");
//        configProps.put("key.serializer", StringSerializer.class);
//        configProps.put("value.serializer", StringSerializer.class);
//        return new DefaultKafkaProducerFactory<>(configProps);
//    }
//
//    @Bean
//    public KafkaTemplate<String, String> stringKafkaTemplate() {
//        return new KafkaTemplate<>(stringProducerFactory());
//    }
//}
