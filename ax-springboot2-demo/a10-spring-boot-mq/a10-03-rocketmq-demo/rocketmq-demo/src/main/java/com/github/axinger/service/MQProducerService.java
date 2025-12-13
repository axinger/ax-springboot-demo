package com.github.axinger.service;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.github.axinger.config.Topic;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Slf4j
@Component
public class MQProducerService {

    @Value("${rocketmq.producer.send-message-timeout}")
    private Integer messageTimeOut;
    // 直接注入使用，用于发送消息到broker服务器
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 发送同步消息（阻塞当前线程，等待broker响应发送结果，这样不太容易丢失消息）
     * （msgBody也可以是对象，sendResult为返回的发送结果）
     */
    public SendResult sendMsg(Object msgBody) {
        SendResult sendResult = rocketMQTemplate.syncSend(Topic.TOPIC_1, MessageBuilder.withPayload(msgBody).build());
        log.info("【sendMsg】sendResult={}", JSON.toJSONString(sendResult));
        return sendResult;
    }

    /**
     * 发送异步消息（通过线程池执行发送到broker的消息任务，执行完后回调：在SendCallback中可处理相关成功失败时的逻辑）
     * （适合对响应时间敏感的业务场景）
     */
    public void sendAsyncMsg(Object msgBody) {
        rocketMQTemplate.asyncSend(Topic.TOPIC_1, MessageBuilder.withPayload(msgBody).build(), new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                // 处理消息发送成功逻辑
            }

            @Override
            public void onException(Throwable throwable) {
                // 处理消息发送异常逻辑
            }
        });
    }

    /**
     * 发送延时消息（上面的发送同步消息，delayLevel的值就为0，因为不延时）
     * 在start版本中 延时消息一共分为18个等级分别为：1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h;
     * delayLevel 值是  1 ~18参数
     */
    public void sendDelayMsg(Object msgBody, int delayLevel) {
        rocketMQTemplate.syncSend(Topic.TOPIC_1, MessageBuilder.withPayload(msgBody).build(), messageTimeOut, delayLevel);
    }

    /**
     * 发送单向消息（只负责发送消息，不等待应答，不关心发送结果，如日志）
     */
    public void sendOneWayMsg(Object msgBody) {
        rocketMQTemplate.sendOneWay(Topic.TOPIC_1, MessageBuilder.withPayload(msgBody).build());
    }

    /**
     * 发送带tag的消息，直接在topic后面加上":tag"
     */
    public SendResult sendTagMsg(Object msgBody) {
        return rocketMQTemplate.syncSend(Topic.TOPIC_1 + ":tag2", MessageBuilder.withPayload(msgBody).build());
    }

    /**
     * MQ半消息, 事务
     *
     * @param topic target topic
     * @param tag   topic's tag
     * @param msg   message
     * @return send status
     */
    public <T extends Serializable> SendStatus sendMessageInTransaction(String topic, String tag, T msg) {

        String destination = String.format("%s:%s", topic, tag);

        Message<T> message = MessageBuilder.withPayload(msg)
                .setHeader("KEYS", IdUtil.simpleUUID())
                .setHeader("DESTINATION", destination)
                .build();

        TransactionSendResult result =
                rocketMQTemplate.sendMessageInTransaction(destination, message, msg);

        // 发送状态
        String sendStatus = result.getSendStatus().name();
        // 本地事务执行状态
        String localTxState = result.getLocalTransactionState().name();
        log.info("send tx message sendStatus:{},localTXState:{}", sendStatus, localTxState);
        return result.getSendStatus();
    }
}
