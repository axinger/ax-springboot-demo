package com.github.axinger.controller;

import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@MessageMapping("/sys")
@Tag(name = "MsgController", description = "发送消息")
public class MsgController {


    // 别人发送 要 /app 开头,
    // 然后这里处理一下, 通知到 /top
    @MessageMapping("/change-notice")
    @SendTo("/topic/notice")
    @Operation(summary = "广播")
    public JSONObject greeting(String value, Principal principal) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("date", LocalDateTime.now());
        jsonObject.put("type", "广播");
        jsonObject.put("data", value);
        System.out.println("principal = " + principal);

        return jsonObject;
    }


}
