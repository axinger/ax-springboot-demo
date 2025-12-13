package com.github.axinger.controller;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class ScheduledController {

    @Autowired
    private SimpMessagingTemplate template;


    //广播推送消息
    @Scheduled(fixedRate = 10000)
    public void sendTopicMessage() {
        System.out.println("后台广播推送！");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("date", LocalDateTime.now());
        jsonObject.put("type", "广播");
        this.template.convertAndSend("/topic/notice", jsonObject);
    }

    //一对一推送消息
    @Scheduled(fixedRate = 3000)
    public void sendQueueMessage() {
        System.out.println("后台一对一推送！");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("date", LocalDateTime.now());
        jsonObject.put("type", "一对一");

        this.template.convertAndSendToUser("/jim", "/queue/getResponse", jsonObject);
    }


}
