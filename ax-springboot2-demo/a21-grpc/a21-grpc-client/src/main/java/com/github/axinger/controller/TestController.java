package com.github.axinger.controller;

import com.github.axinger.config.ChatService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Setter
@Service
@Slf4j
@RestController
public class TestController {

    @Autowired
    private ChatService ChatService;

    @GetMapping("/pushServer")
    public String pushServer(String message) {
        boolean b = ChatService.sendMessage(message);
        return b ? "成功" : "失败";
    }
}
