//package com.github.axinger.config;
//
//import org.apache.rocketmq.client.producer.TransactionMQProducer;
//import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
//import org.apache.rocketmq.spring.core.RocketMQTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RocketMQConfig {
//
//    @Autowired
//    private RocketMQTemplate rocketMQTemplate;
//    @Value("${rocketmq.name-server}")
//    private String nameServer;
//    @Value("${rocketmq.producer.group-name}")
//    private String producerGroupName;
//
//    @Bean
//    public RocketMQLocalTransactionListener transactionListener() {
//        return new MyTransactionListener(rocketMQTemplate);
//    }
//
//    /**
//     * 事务消息生产者
//     *
//     * @return
//     */
//    @Bean
//    public TransactionMQProducer transactionMQProducer() {
//        TransactionMQProducer producer = new TransactionMQProducer(producerGroupName);
//        producer.setNamesrvAddr(nameServer);
//        return producer;
//    }
//
//    /**
//     * RocketMQ连接组件
//     *
//     * @param transactionMQProducer
//     * @return
//     */
//    @Bean
//    public RocketMQTemplate rocketTransactionMQTemplate(TransactionMQProducer transactionMQProducer) {
//        RocketMQTemplate rocketMQTemplate = new RocketMQTemplate();
//        rocketMQTemplate.setProducer(transactionMQProducer);
//        return rocketMQTemplate;
//    }
//
//}
