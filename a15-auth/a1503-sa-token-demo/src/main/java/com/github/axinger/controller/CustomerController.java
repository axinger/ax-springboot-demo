package com.github.axinger.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/// CustomerController (客户管理)
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    /**
     * 获取客户列表 - 需要customer:info:read权限
     */
    @GetMapping
    public ResponseEntity<?> getCustomers() {
        // 业务逻辑实现
        return ResponseEntity.ok().build();
    }

    /**
     * 获取客户详情 - 需要customer:info:read权限
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long id) {
        // 业务逻辑实现
        return ResponseEntity.ok().build();
    }

    /**
     * 更新客户信息 - 需要customer:info:read权限
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id) {
        // 业务逻辑实现
        return ResponseEntity.ok().build();
    }

    /**
     * 分配客户 - 需要customer:assign权限
     */
    @PostMapping("/assign")
    public ResponseEntity<?> assignCustomer() {
        // 业务逻辑实现
        return ResponseEntity.ok().build();
    }

    /**
     * 获取分配给当前用户的客户 - 需要customer:info:read权限
     */
    @GetMapping("/my-customers")
    public ResponseEntity<?> getMyCustomers() {
        // 业务逻辑实现
        return ResponseEntity.ok().build();
    }
}
