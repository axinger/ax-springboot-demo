package com.github.axinger.controller;

import com.axing.common.response.dto.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    /**
     * 更新订单接口 - 需要order:update权限
     */
    @PutMapping("/{id}")
    public Result<?> updateOrder(@PathVariable Long id) {
        // 权限验证在拦截器中完成
        // 这里直接执行业务逻辑
        return Result.success("成功查看数据");
    }

    /**
     * 销售审批接口 - 需要sales:order:approve权限或ROLE_SALES_DIRECTOR角色
     */
    @PostMapping("/approve")
    public Result<?> approveOrder() {
        // 权限验证在拦截器中完成
        return Result.success("成功查看数据");
    }
}
