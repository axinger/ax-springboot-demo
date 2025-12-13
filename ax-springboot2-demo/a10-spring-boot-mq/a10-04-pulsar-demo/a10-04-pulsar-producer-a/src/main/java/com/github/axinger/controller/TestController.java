package com.github.axinger.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.github.axinger.model.Person;
import com.github.axinger.producer.PulsarProducer;
import com.github.axinger.topic.Topic;
import io.github.majusko.pulsar.producer.PulsarTemplate;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


@RestController
public class TestController {

    @Autowired
    private PulsarProducer pulsarProducer;

    @Autowired
    private PulsarTemplate<Person> template;


    @GetMapping(value = "/sendMessage")
    public void sendMessage() {
        Map<String, Object> map = new HashMap<>();
        map.put("date", LocalDateTimeUtil.format(LocalDateTime.now(), "yyyy-MM-dd HH:mm:ss"));
        map.put("data", "发送普通消息");
        pulsarProducer.send(map);
    }


    @GetMapping(value = "/deliverAfter")
    @ResponseBody
    public void deliverAfter() {
        // Map map = new HashMap<>();
        // map.put("date", LocalDateTimeUtil.format(LocalDateTime.now(), "yyyy-MM-dd HH:mm:ss"));
        // map.put("data", "发送普通消息");
        // pulsarProducer.send(map);
        pulsarProducer.deliverAfter();

    }


    @GetMapping("/more")
    public void more() throws PulsarClientException {
        for (int i = 0; i < 20; i++) {
            Person person = new Person();
            person.setId(i);
            person.setName("jim_" + i);
            person.setAge(i);
//            template.send(Topic.SHARED_TOPIC, person);
            CompletableFuture<MessageId> completableFuture = template.sendAsync(Topic.SHARED_TOPIC, person);
            // 通过异步回调得知消息发送成功与否
            completableFuture.whenComplete(((messageId, throwable) -> {
                if (null != throwable) {
                    System.out.println("delivery failed, value: " + person);
                    // 此处可以添加延时重试的逻辑
                } else {
                    System.out.println("delivered msg " + messageId + ", value:" + person);
                }
            }));
        }
    }
}
