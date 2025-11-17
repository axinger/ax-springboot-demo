package com.github.axinger.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/// ApiController (API管理 - 用于前端动态生成菜单和权限控制)
@RestController
@RequestMapping("/api/metadata")
public class ApiMetadataController {

    /**
     * 获取所有API元数据 - 需要api:manage权限
     */
    @GetMapping("/apis")
    public ResponseEntity<?> getAllApis() {
        // 业务逻辑实现
        return ResponseEntity.ok().build();
    }

    /**
     * 获取当前用户的API权限 - 不需要特殊权限，只需登录
     */
    @GetMapping("/my-permissions")
    public ResponseEntity<?> getUserApiPermissions() {
        // 业务逻辑实现
        return ResponseEntity.ok().build();
    }

    /**
     * 获取菜单和权限配置 - 不需要特殊权限，只需登录
     */
    @GetMapping("/menu-config")
    public ResponseEntity<?> getMenuConfig() {
        // 业务逻辑实现
        return ResponseEntity.ok().build();
    }
}
