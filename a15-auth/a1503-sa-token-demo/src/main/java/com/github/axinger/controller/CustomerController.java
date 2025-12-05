package com.github.axinger.controller;

import com.axing.common.response.dto.Result;
import org.springframework.web.bind.annotation.*;

/// CustomerController (客户管理)
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    /**
     * 获取客户列表 - 需要customer:info:read权限
     */
    @GetMapping
    public Result<?> getCustomers() {
        // 业务逻辑实现
        return Result.success("成功查看数据");
    }

    /**
     * 获取客户详情 - 需要customer:info:read权限
     */
    @GetMapping("/{id}")
    public Result<?> getCustomerById(@PathVariable Long id) {
        // 业务逻辑实现
        return Result.success("成功查看数据");
    }

    /**
     * 更新客户信息 - 需要customer:info:read权限
     */
    @PutMapping("/{id}")
    public Result<?> updateCustomer(@PathVariable Long id) {
        // 业务逻辑实现
        return Result.success("成功查看数据");
    }

    /**
     * 分配客户 - 需要customer:assign权限
     */
    @PostMapping("/assign")
    public Result<?> assignCustomer() {
        // 业务逻辑实现
        return Result.success("成功查看数据");
    }

    /**
     * 获取分配给当前用户的客户 - 需要customer:info:read权限
     */
    @GetMapping("/my-customers")
    public Result<?> getMyCustomers() {
        // 业务逻辑实现
        return Result.success("成功查看数据");
    }
}
