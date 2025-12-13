package com.github.axinger.controller;

import com.github.axinger.pojo.MessageDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author JCccc
 * @Description
 * @Date 2021/8/20 8:53
 */
@RestController
@Tag(name = "TestController", description = "发送消息")
public class TestController {
    @Autowired
    public SimpMessagingTemplate template;

    /**
     * 广播
     *
     * @param msg
     */
    @ResponseBody
    @PostMapping("/pushToAll")
    @Operation(summary = "广播")
    public void subscribe(@RequestBody MessageDTO msg) {
        template.convertAndSend("/topic/notice", msg);
    }


    @ResponseBody
    @PostMapping("/pushToOne")
    @Operation(summary = "单点发送消息")
    public void queue(@RequestBody MessageDTO msg) {
        System.out.println("单点发送消息 Message = " + msg);

        /*使用convertAndSendToUser方法，第一个参数为用户id，此时js中的订阅地址为
        "/user/" + 用户Id + "/message",其中"/user"是固定的*/
        /// 也许看到这里，你会觉得很奇怪，为什么我们推的主题是 /message，但是前端订阅的却是
        //    "/user/" + 用户Id + "/message"
        template.convertAndSendToUser(msg.getTo(), "/message", msg);

    }


}
