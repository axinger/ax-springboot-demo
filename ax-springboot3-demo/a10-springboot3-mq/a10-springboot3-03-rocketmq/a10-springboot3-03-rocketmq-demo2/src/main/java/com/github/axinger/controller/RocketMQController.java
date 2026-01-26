package com.github.axinger.controller;

import com.alibaba.fastjson2.JSON;
import com.github.axinger.config.Topic;
import com.github.axinger.model.MessageWrapper;
import com.github.axinger.model.User;
import com.github.axinger.service.MQProducerService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.IntStream;

@Slf4j
@RestController
@RequestMapping("/rocketmq")
public class RocketMQController {

    @Autowired
    private MQProducerService mqProducerService;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @GetMapping("/send")
    void send() {
        for (int i = 0; i < 20; i++) {
            User user = new User();
            user.setName("jim_" + i);
            user.setAge(i);
            /// // 同步发送
//            rocketMQTemplate.convertAndSend(Topic.TOPIC_1 + ":" + Topic.Tag_1, user);
            rocketMQTemplate.asyncSend(Topic.TOPIC_1 + ":" + Topic.Tag_1, user, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    // 成功 ACK
                }

                @Override
                public void onException(Throwable throwable) {
                    // 失败处理（相当于 NACK）
                }
            });
        }
    }

    @GetMapping("/send2")
    void send2() {
        User user = new User();
        user.setName("jim");
        user.setAge(10);
        rocketMQTemplate.convertAndSend(Topic.TOPIC_1 + ":" + Topic.Tag_2, user);
    }

    @GetMapping("/sendMsg")
    void sendMsg() {
        User user = new User();
        user.setName("jim");
        user.setAge(10);
        mqProducerService.sendMsg(user);
    }

    @GetMapping("/sendAsyncMsg")
    void sendAsyncMsg() {
        User user = new User();
        user.setName("jim");
        user.setAge(10);
        mqProducerService.sendAsyncMsg(user);
    }

    @GetMapping("/sendDelayMsg")
    void sendDelayMsg() {
        User user = new User();
        user.setName("延迟消息");
        user.setAge(10);
        mqProducerService.sendDelayMsg(user, 2);
    }

    @GetMapping("/sendOneWayMsg")
    void sendOneWayMsg() {
        User user = new User();
        user.setName("jim");
        user.setAge(10);
        mqProducerService.sendOneWayMsg(user);
    }

    @GetMapping("/sendTagMsg")
    void sendTagMsg() {
        User user = new User();
        user.setName("带有tag的字符消息");
        user.setAge(10);
        mqProducerService.sendTagMsg(user);
    }

    @GetMapping("/noOrder")
    public Boolean updateUser() {

        String userTopic = Topic.sync_user_topic;

        IntStream.range(0, 10).forEach(i -> {

            MessageWrapper messageSend = MessageWrapper.builder()
                    .keys(userTopic).message("用户向钱包中转入100元，短信通知用户目前剩余金额100元：" + i)
                    .timestamp(System.currentTimeMillis()).build();

            MessageWrapper messageSend1 = MessageWrapper.builder()
                    .keys(userTopic).message("用户下单商品消费50元，短信通知用户目前剩余金额50元：" + i)
                    .timestamp(System.currentTimeMillis()).build();

            rocketMQTemplate.syncSend(userTopic, messageSend);

            rocketMQTemplate.syncSend(userTopic, messageSend1);
        });


        return true;
    }


    @RequestMapping("/transaction")
    public String transaction() {
        Message<String> message = MessageBuilder.withPayload("事务消息")
                .setHeader(RocketMQHeaders.KEYS, 1)
                .setHeader("money", 10)
                .setHeader(RocketMQHeaders.TRANSACTION_ID, 100)
                .build();
        TransactionSendResult transactionSendResult = rocketMQTemplate.sendMessageInTransaction("transaction", message, null);
        return "事务消息";
    }

    @SneakyThrows
    public void sendDelayMQ() {

        org.apache.rocketmq.common.message.Message message = new org.apache.rocketmq.common.message.Message();
        long delayTimestamp = System.currentTimeMillis() + 11_000;
// 指定发送的时间：11秒后
        message.setDeliverTimeMs(delayTimestamp);
        message.setTopic("delay_topic_test3");
        message.setBody("Hello, Delay RocketMQ!".getBytes());
        rocketMQTemplate.getProducer().send(message);
        log.info("开始发送延迟消息：{}", new String(message.getBody()));

    }

    @SneakyThrows
    public void sendDelayMQ1() {
        org.apache.rocketmq.common.message.Message message = new org.apache.rocketmq.common.message.Message();
// 指定延迟的时间：毫秒
        message.setDelayTimeMs(10_000);
        message.setTopic("delay_topic_test3");
        message.setBody("Hello, Delay RocketMQ!".getBytes());
        rocketMQTemplate.getProducer().send(message);
        log.info("开始发送延迟消息：{}", new String(message.getBody()));

    }

    @SneakyThrows
    public void sendDelayMQ2() {
        Message<String> msg = MessageBuilder.withPayload("测试精确的延时消息").build();
        log.info("开始发送延迟消息--1：{}", msg.getPayload());
        SendResult sendResult = rocketMQTemplate.syncSendDelayTimeSeconds("delay_topic_test3", msg, 8);
        log.info("消息发送结果：{}", JSON.toJSONString(sendResult));

    }

}
