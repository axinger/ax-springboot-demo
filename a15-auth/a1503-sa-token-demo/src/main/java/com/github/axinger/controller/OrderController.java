package com.github.axinger.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    /**
     * 更新订单接口 - 需要order:update权限
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id) {
        // 权限验证在拦截器中完成
        // 这里直接执行业务逻辑
        return ResponseEntity.ok().build();
    }

    /**
     * 销售审批接口 - 需要sales:order:approve权限或ROLE_SALES_DIRECTOR角色
     */
    @PostMapping("/approve")
    public ResponseEntity<?> approveOrder() {
        // 权限验证在拦截器中完成
        return ResponseEntity.ok().build();
    }
}
