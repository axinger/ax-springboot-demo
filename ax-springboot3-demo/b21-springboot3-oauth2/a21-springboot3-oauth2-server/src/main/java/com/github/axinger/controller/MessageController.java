package com.github.axinger.controller;

import com.github.axinger.entity.SysMessage;
import com.github.axinger.service.SysMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 服务器端消息管理控制器（内部演示）
 */
@RestController
@RequestMapping("/server/messages")
@RequiredArgsConstructor
public class MessageController {

    private final SysMessageService messageService;

    @GetMapping
    public List<SysMessage> list() {
        return messageService.list();
    }

    @GetMapping("/{id}")
    public SysMessage getById(@PathVariable Long id) {
        return messageService.getById(id);
    }

    @PostMapping
    public boolean save(@RequestBody SysMessage message) {
        return messageService.save(message);
    }

    @PutMapping("/{id}")
    public boolean update(@PathVariable Long id, @RequestBody SysMessage message) {
        message.setId(id);
        return messageService.updateById(message);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return messageService.removeById(id);
    }
}
