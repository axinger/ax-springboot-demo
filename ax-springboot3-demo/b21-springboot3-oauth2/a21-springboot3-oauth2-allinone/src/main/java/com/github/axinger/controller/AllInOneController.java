package com.github.axinger.controller;

import com.github.axinger.entity.SysMessage;
import com.github.axinger.service.SysMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

/**
 * All-in-One 单体应用 Web UI 控制器
 * 提供首页和消息管理页面，直接调用资源服务器 Service
 */
@Controller
@RequiredArgsConstructor
public class AllInOneController {

    private final SysMessageService messageService;

    /**
     * 首页
     */
    @GetMapping({"/", "/index"})
    public String index(Model model, Principal principal) {
        model.addAttribute("loggedIn", principal != null);
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }
        return "index";
    }

    /**
     * 消息列表页面
     */
    @GetMapping("/messages")
    public String messages(Model model) {
        List<SysMessage> messages = messageService.list();
        model.addAttribute("messages", messages);
        return "messages";
    }

    /**
     * 创建消息
     */
    @PostMapping("/messages")
    public String createMessage(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam(required = false, defaultValue = "NORMAL") String messageType,
            Principal principal) {
        SysMessage message = new SysMessage();
        message.setTitle(title);
        message.setContent(content);
        message.setMessageType(messageType);
        message.setStatus("PUBLISHED");
        message.setAuthorId(1L);
        messageService.save(message);
        return "redirect:/messages";
    }
}
