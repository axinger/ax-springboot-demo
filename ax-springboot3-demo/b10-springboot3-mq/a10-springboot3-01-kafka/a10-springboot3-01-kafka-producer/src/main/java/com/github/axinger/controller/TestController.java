package com.github.axinger.controller;

import cn.hutool.core.util.IdUtil;
import com.github.axinger.api.Topic;
import com.github.axinger.api.model.MessageUserDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RestController
public class TestController {

    @Autowired
    private KafkaTemplate<String, MessageUserDTO> kafkaTemplate;

    @SneakyThrows
    @GetMapping("/test1")
    void test1(String groupId) {

        MessageUserDTO user = new MessageUserDTO();
        user.setGuild(IdUtil.fastSimpleUUID());
        user.setName("jim");
        user.setCurrentTime(new Date());

//        ListenableFuture<SendResult<String, UserDTO>> send = kafkaTemplate.send(Topic.USER_DTO, user);

        // 创建ProducerRecord并添加头信息
        ProducerRecord<String, MessageUserDTO> record = new ProducerRecord<>(Topic.USER_DTO, user);
        record.headers().add("target-consumer", groupId.getBytes()); // 指定发送给group1

        ListenableFuture<SendResult<String, MessageUserDTO>> send = kafkaTemplate.send(record);

//        send.addCallback(
//                sendResult -> {
//                    log.info("发送消息成功===================");
//                    RecordMetadata recordMetadata = sendResult.getRecordMetadata();
//                    log.info("recordMetadata = {}", recordMetadata);
//                    UserDTO value = sendResult.getProducerRecord().value();
//                    log.info("value = {}", value);
//                },
//                e -> {
//                    log.error("发送消息失败: {}", e.getMessage(), e);
//                }
//        );
        // 添加回调以处理发送结果
        send.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(SendResult<String, MessageUserDTO> result) {
                System.out.println("发送成功result = " + result);
                MessageUserDTO value = result.getProducerRecord().value();
                RecordMetadata recordMetadata = result.getRecordMetadata();
                System.out.println("recordMetadata = " + recordMetadata);
                System.out.println("value = " + value);
            }

            @Override
            public void onFailure(Throwable ex) {
                System.err.println("发送失败: " + ex);
            }
        });

    }

    @SneakyThrows
    @GetMapping("/test2")
    void test2() {
        MessageUserDTO user = new MessageUserDTO();
        user.setName("jim");
        user.setAge(11);
        user.setCurrentTime(new Date());
        ListenableFuture<SendResult<String, MessageUserDTO>> sent = kafkaTemplate.send(Topic.USER_JSON, user);

        // 添加回调以处理发送结果
        sent.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(SendResult<String, MessageUserDTO> result) {

                System.out.println("发送成功result = " + result);
            }

            @Override
            public void onFailure(Throwable ex) {
                System.err.println("发送失败: " + ex.getMessage());
            }
        });


    }


}
