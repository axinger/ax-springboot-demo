package com.github.axinger.controller;

import com.github.axinger.entity.SysMessage;
import com.github.axinger.service.SysMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息管理 REST API 控制器
 * 受OAuth2保护的资源接口
 */
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageApiController {

    private final SysMessageService messageService;

    /**
     * 获取消息列表
     * 需要 message.read 权限
     */
    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_message.read')")
    public List<SysMessage> list() {
        return messageService.list();
    }

    /**
     * 获取单个消息
     * 需要 message.read 权限
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_message.read')")
    public SysMessage getById(@PathVariable Long id) {
        return messageService.getById(id);
    }

    /**
     * 创建消息
     * 需要 message.write 权限
     */
    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_message.write')")
    public boolean save(@RequestBody SysMessage message) {
        return messageService.save(message);
    }

    /**
     * 更新消息
     * 需要 message.write 权限
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_message.write')")
    public boolean update(@PathVariable Long id, @RequestBody SysMessage message) {
        message.setId(id);
        return messageService.updateById(message);
    }

    /**
     * 删除消息
     * 需要 message.write 权限
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_message.write')")
    public boolean delete(@PathVariable Long id) {
        return messageService.removeById(id);
    }
}
