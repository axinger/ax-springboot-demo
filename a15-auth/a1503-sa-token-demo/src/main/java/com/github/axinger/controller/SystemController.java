package com.github.axinger.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/// SystemController (系统管理)
@RestController
@RequestMapping("/api/system")
public class SystemController {

    /**
     * 获取用户列表 - 需要user:manage权限
     */
    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        // 业务逻辑实现
        return ResponseEntity.ok().build();
    }

    /**
     * 创建新用户 - 需要user:manage权限
     */
    @PostMapping("/users")
    public ResponseEntity<?> createUser() {
        // 业务逻辑实现
        return ResponseEntity.ok().build();
    }

    /**
     * 更新用户信息 - 需要user:manage权限
     */
    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id) {
        // 业务逻辑实现
        return ResponseEntity.ok().build();
    }

    /**
     * 获取角色列表 - 需要role:manage权限
     */
    @GetMapping("/roles")
    public ResponseEntity<?> getRoles() {
        // 业务逻辑实现
        return ResponseEntity.ok().build();
    }

    /**
     * 获取权限列表 - 需要permission:manage权限
     */
    @GetMapping("/permissions")
    public ResponseEntity<?> getPermissions() {
        // 业务逻辑实现
        return ResponseEntity.ok().build();
    }

    /**
     * 获取API列表 - 需要api:manage权限
     */
    @GetMapping("/apis")
    public ResponseEntity<?> getApis() {
        // 业务逻辑实现
        return ResponseEntity.ok().build();
    }

    /**
     * 获取当前登录用户信息 - 不需要特殊权限，只需登录
     */
    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUserInfo() {
        // 业务逻辑实现
        return ResponseEntity.ok().build();
    }
}
