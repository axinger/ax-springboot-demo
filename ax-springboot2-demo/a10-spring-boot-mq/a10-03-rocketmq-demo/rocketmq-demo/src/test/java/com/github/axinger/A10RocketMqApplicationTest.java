package com.github.axinger;

import com.github.axinger.config.Topic;
import com.github.axinger.model.User;
import com.github.axinger.service.MQProducerService;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class A10RocketMqApplicationTest {

    @Autowired
    private MQProducerService mqProducerService;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Test
    void send() {
        User user = new User();
        user.setName("jim");
        user.setAge(10);
        rocketMQTemplate.convertAndSend(Topic.TOPIC_1 + ":" + Topic.Tag_1, user);
    }

    @Test
    void sendMsg() {
        User user = new User();
        user.setName("jim");
        user.setAge(10);
        mqProducerService.sendMsg(user);
    }

    @Test
    void sendAsyncMsg() {
        User user = new User();
        user.setName("jim");
        user.setAge(10);
        mqProducerService.sendAsyncMsg(user);
    }

    @Test
    void sendDelayMsg() {
        User user = new User();
        user.setName("延迟消息");
        user.setAge(10);
        mqProducerService.sendDelayMsg(user, 5);
    }

    @Test
    void sendOneWayMsg() {
        User user = new User();
        user.setName("jim");
        user.setAge(10);
        mqProducerService.sendOneWayMsg(user);
    }

    @Test
    void sendTagMsg() {
        User user = new User();
        user.setName("jim");
        user.setAge(10);
        mqProducerService.sendTagMsg(user);
    }

    @Test
    public void test10() {
        //单向发送消息
        //单向传输用于需要中等可靠性的情况，例如日志收集。
        rocketMQTemplate.sendOneWay("springTopic", "Hello, World!");
    }

    @Test
    public void test101() {
        //单向发送消息,有序消息
        //单向传输用于需要中等可靠性的情况，例如日志收集。
        rocketMQTemplate.sendOneWayOrderly("springTopic", "Hello, World!", "");
    }


    @Test
    public void test11() throws InterruptedException {
        //异步发送消息
        //异步发送消息，一般用于消息耗时非常短的场景，例如日志收集。
        rocketMQTemplate.asyncSend("springTopic", "Hello, World!", new SendCallback() {
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

    @Test
    public void test12() throws InterruptedException {
        //同步发送消息
        //同步发送消息，一般用于消息耗时较长的场景，例如订单支付场景。
        SendResult sendResult = rocketMQTemplate.syncSend("springTopic", "Hello, World!");
        System.out.println(sendResult);
    }

    @Test
    public void test121() throws InterruptedException {
        //发送有序消息
        rocketMQTemplate.syncSendOrderly("orderly_topic", MessageBuilder.withPayload("Hello, World").build(), "hashkey");
    }

    @Test
    public void test13() throws InterruptedException {
//        发送事务消息

        Message msg = MessageBuilder.withPayload("rocketMQTemplate transactional message ").build();

// 第一个参数必须与@RocketMQTransactionListener的成员字段’transName’相同

        rocketMQTemplate.sendMessageInTransaction("test-topic", msg, null);
    }

    @Test
    public void test14() throws InterruptedException {
//        发送特殊标签（tag）消息

        String msgExtTopic = "msgExtTopic";
        rocketMQTemplate.convertAndSend(msgExtTopic + ":tag0", "I’m from tag0"); // tag0 不是消费者选择的，可以通过tag过滤掉

        rocketMQTemplate.convertAndSend(msgExtTopic + ":tag1", "I’m from tag1");
    }

    @Test
    public void test15() throws InterruptedException {
        rocketMQTemplate.convertAndSend("delayTopic:tag0", "delay message", message -> {

            return message;
        });
    }

    @Test
    public void test16() throws InterruptedException {

        String springTopic = "springTopic";
        //发送批量消息

        List<Message<?>> msgs = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            msgs.add(MessageBuilder
                    .withPayload("Hello RocketMQ Batch Msg#" + i).
                    setHeader(RocketMQHeaders.KEYS, "KEY_" + i)
                    .build());
        }

        SendResult sr = rocketMQTemplate.syncSend(springTopic, msgs, 60000);
    }

}
