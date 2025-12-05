package com.github.axinger.controller;

import com.axing.common.response.dto.Result;
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
    public Result<?> getSalesDashboard() {
        // 业务逻辑实现
        return Result.success("成功查看数据");
    }

    /**
     * 获取库存仪表盘数据 - 需要stock:read权限
     */
    @GetMapping("/inventory")
    public Result<?> getInventoryDashboard() {
        // 业务逻辑实现
        return Result.success("成功查看数据");
    }

    /**
     * 获取订单仪表盘数据 - 需要order:read权限
     */
    @GetMapping("/orders")
    public Result<?> getOrdersDashboard() {
        // 业务逻辑实现
        return Result.success("成功查看数据");
    }

    /**
     * 获取团队绩效仪表盘 - 需要team:performance:read权限
     */
    @GetMapping("/team-performance")
    public Result<?> getTeamPerformanceDashboard() {
        // 业务逻辑实现
        return Result.success("成功查看数据");
    }
}
