package com.github.axinger.controller;

import com.github.axinger.config.Topic;
import com.github.axinger.model.User;
import com.github.axinger.service.MQProducerService;
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

@RestController
@RequestMapping("/rocketmq")
public class TestController {

    @Autowired
    private MQProducerService mqProducerService;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @GetMapping("/send")
    void send() {
        for (int i = 0; i < 20; i++) {
            User user = new User();
            user.setId(i);
            user.setName("jim_" + i);
            user.setAge(i);
            /// // 同步发送
            rocketMQTemplate.convertAndSend(Topic.TOPIC_1 + ":" + Topic.Tag_1, user);


//            rocketMQTemplate.asyncSend(Topic.TOPIC_1 + ":" + Topic.Tag_1, user, new SendCallback() {
//                @Override
//                public void onSuccess(SendResult sendResult) {
//                    // 成功 ACK
//                    System.out.println("成功 = " + sendResult);
//
//                    SendStatus sendStatus = sendResult.getSendStatus();
//                    System.out.println("sendStatus = " + sendStatus);
//                }
//
//                @Override
//                public void onException(Throwable throwable) {
//                    // 失败处理（相当于 NACK）
//                    System.out.println("throwable = " + throwable);
//                }
//            });
        }
    }


    @GetMapping("/send1")
    void send1() {
        User user = new User();
        user.setId(1);
        user.setName("jim");
        user.setAge(10);

        rocketMQTemplate.asyncSend(Topic.TOPIC_1 + ":" + Topic.Tag_1, user, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                // 成功 ACK
                System.out.println("成功 = " + sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                // 失败处理（相当于 NACK）
                System.out.println("throwable = " + throwable);
            }
        });
    }

    @GetMapping("/send2")
    void send2() {
        User user = new User();
        user.setId(1);
        user.setName("jim");
        user.setAge(10);
//        rocketMQTemplate.convertAndSend(Topic.TOPIC_2 + ":" + Topic.Tag_2, user);

        // 确保使用正确的 Tag 发送方式
        rocketMQTemplate.convertAndSend(Topic.TOPIC_2 + ":" + Topic.Tag_2, user);
    }

    @GetMapping("/send3")
    void send3() {
        User user = new User();
        user.setId(2);
        user.setName("tom");
        user.setAge(10);

        rocketMQTemplate.convertAndSend(Topic.TOPIC_2 + ":" + Topic.Tag_3, user);


        // 或者使用 MessageBuilder 设置属性
//        Message<User> msg = MessageBuilder.withPayload(user)
//                .setHeader(MessageConst.PROPERTY_TAGS, Topic.Tag_3)
//
//                .build();
//
//        rocketMQTemplate.send(Topic.TOPIC_2, msg);
//        rocketMQTemplate.convertAndSend(Topic.TOPIC_2, msg);
    }

    @GetMapping("/send4")
    void send4() {
        User user = new User();
        user.setId(4);
        user.setName("tom");
        user.setAge(10);
        String o = rocketMQTemplate.sendAndReceive(Topic.TOPIC_3, user, String.class, 6000);
        System.out.println("o = " + o);
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

}
