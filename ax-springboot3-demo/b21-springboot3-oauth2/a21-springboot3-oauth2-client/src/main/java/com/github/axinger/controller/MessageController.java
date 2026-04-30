package com.github.axinger.controller;

import com.github.axinger.service.ResourceServerClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息管理控制器
 * 调用资源服务器的消息API
 */
@Controller
@RequiredArgsConstructor
public class MessageController {

    private final ResourceServerClient resourceServerClient;

    /**
     * 消息列表页面
     */
    @GetMapping("/messages")
    public String messages(Model model) {
        try {
            List<Map<String, Object>> messages = resourceServerClient.getMessages();
            model.addAttribute("messages", messages);
        } catch (Exception e) {
            model.addAttribute("error", "获取消息失败: " + e.getMessage());
        }
        return "messages";
    }

    /**
     * 创建消息
     */
    @PostMapping("/messages")
    public String createMessage(@RequestParam String title, @RequestParam String content, @RequestParam(required = false, defaultValue = "NORMAL") String messageType) {
        Map<String, Object> message = new HashMap<>();
        message.put("title", title);
        message.put("content", content);
        message.put("messageType", messageType);
        message.put("status", "PUBLISHED");
        message.put("authorId", 1);
        resourceServerClient.createMessage(message);
        return "redirect:/messages";
    }
}
