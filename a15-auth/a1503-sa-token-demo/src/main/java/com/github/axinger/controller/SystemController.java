package com.github.axinger.controller;

import com.axing.common.response.dto.Result;
import org.springframework.web.bind.annotation.*;

/// SystemController (系统管理)
@RestController
@RequestMapping("/api/system")
public class SystemController {

    /**
     * 获取用户列表 - 需要user:manage权限
     */
    @GetMapping("/users")
    public Result<?> getUsers() {
        // 业务逻辑实现
        return Result.success("成功查看数据");
    }

    /**
     * 创建新用户 - 需要user:manage权限
     */
    @PostMapping("/users")
    public Result<?> createUser() {
        // 业务逻辑实现
        return Result.success("成功查看数据");
    }

    /**
     * 更新用户信息 - 需要user:manage权限
     */
    @PutMapping("/users/{id}")
    public Result<?> updateUser(@PathVariable Long id) {
        // 业务逻辑实现
        return Result.success("成功查看数据");
    }

    /**
     * 获取角色列表 - 需要role:manage权限
     */
    @GetMapping("/roles")
    public Result<?> getRoles() {
        // 业务逻辑实现
        return Result.success("成功查看数据");
    }

    /**
     * 获取权限列表 - 需要permission:manage权限
     */
    @GetMapping("/permissions")
    public Result<?> getPermissions() {
        // 业务逻辑实现
        return Result.success("成功查看数据");
    }

    /**
     * 获取API列表 - 需要api:manage权限
     */
    @GetMapping("/apis")
    public Result<?> getApis() {
        // 业务逻辑实现
        return Result.success("成功查看数据");
    }

    /**
     * 获取当前登录用户信息 - 不需要特殊权限，只需登录
     */
    @GetMapping("/current-user")
    public Result<?> getCurrentUserInfo() {
        // 业务逻辑实现
        return Result.success("成功查看数据");
    }
}
