package com.github.axinger.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// DashboardController (仪表盘)
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    /**
     * 获取销售仪表盘数据 - 需要sales:data:read权限
     */
    @GetMapping("/sales")
    public ResponseEntity<?> getSalesDashboard() {
        // 业务逻辑实现
        return ResponseEntity.ok().build();
    }

    /**
     * 获取库存仪表盘数据 - 需要stock:read权限
     */
    @GetMapping("/inventory")
    public ResponseEntity<?> getInventoryDashboard() {
        // 业务逻辑实现
        return ResponseEntity.ok().build();
    }

    /**
     * 获取订单仪表盘数据 - 需要order:read权限
     */
    @GetMapping("/orders")
    public ResponseEntity<?> getOrdersDashboard() {
        // 业务逻辑实现
        return ResponseEntity.ok().build();
    }

    /**
     * 获取团队绩效仪表盘 - 需要team:performance:read权限
     */
    @GetMapping("/team-performance")
    public ResponseEntity<?> getTeamPerformanceDashboard() {
        // 业务逻辑实现
        return ResponseEntity.ok().build();
    }
}
