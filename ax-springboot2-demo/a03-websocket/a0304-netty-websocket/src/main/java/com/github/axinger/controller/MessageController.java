package com.github.axinger.controller;

import com.alibaba.fastjson2.JSON;
import com.github.axinger.cnfig.MyWebSocket;
import com.github.axinger.model.TestDTO;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/message")
public class MessageController {

    @GetMapping("/all")
    public void all() {

        MyWebSocket.sendAllMessage("群发消息");
    }

    @PostMapping("/plc")
    public Object plc(@RequestBody TestDTO msg) {
        MyWebSocket.sendMessage("1714471285016895490", JSON.toJSONString(msg));
        return Map.of("result", "发送成功");
    }
}
