package com.axinger.modulith;

import com.axinger.shared.CustomerId;
import com.axinger.shared.OrderId;
import org.springframework.web.bind.annotation.*;

/**
 * 演示控制器 - 展示 Spring Modulith 模块间协作
 */
@RestController
@RequestMapping("/api/demo")
public class DemoController {

    /**
     * 获取系统信息
     */
    @GetMapping("/info")
    public String getSystemInfo() {
        return "Spring Modulith 企业级模块化架构演示\n" +
               "==================================\n" +
               "核心模块: \n" +
               "  - order-module: 订单管理\n" +
               "  - customer-module: 客户管理\n" +
               "  - inventory-module: 库存管理\n" +
               "  - payment-module: 支付处理\n" +
               "  - notification-module: 消息通知\n" +
               "  - shared-kernel: 共享内核\n\n" +
               "架构特性: \n" +
               "  - 模块化设计\n" +
               "  - 领域驱动设计(DDD)\n" +
               "  - 明确的模块边界\n" +
               "  - 事件驱动架构\n" +
               "  - 架构约束验证";
    }

    /**
     * 演示值对象创建
     */
    @GetMapping("/demo-values")
    public String demoValueObjects() {
        OrderId orderId = OrderId.create();
        CustomerId customerId = CustomerId.create();

        return "值对象演示:\n" +
               "订单ID: " + orderId + "\n" +
               "客户ID: " + customerId + "\n" +
               "订单ID字符串: " + orderId.toString() + "\n" +
               "客户ID字符串: " + customerId.toString();
    }

    /**
     * 演示模块信息
     */
    @GetMapping("/modules")
    public String getModuleInfo() {
        return "Spring Modulith 模块信息\n" +
               "========================\n" +
               "1. shared-kernel - 共享内核模块\n" +
               "   提供: OrderId, CustomerId, Money 值对象\n\n" +
               "2. order-module - 订单模块\n" +
               "   职责: 订单生命周期管理\n" +
               "   依赖: customer-module, inventory-module, payment-module\n\n" +
               "3. customer-module - 客户模块\n" +
               "   职责: 客户信息管理\n   核心: Customer, CustomerName, Email, PhoneNumber, Address\n\n" +
               "4. inventory-module - 库存模块\n" +
               "   职责: 产品库存管理\n" +
               "   核心: Product, Inventory, 库存操作\n\n" +
               "5. payment-module - 支付模块\n" +
               "   职责: 支付处理\n" +
               "   核心: Payment, PaymentMethod, Refund\n\n" +
               "6. notification-module - 通知模块\n" +
               "   职责: 消息通知\n" +
               "   核心: Notification, EmailNotification, SmsNotification";
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public String health() {
        return "✅ Spring Modulith 应用运行正常\n" +
               "🕒 当前时间: " + java.time.LocalDateTime.now() + "\n" +
               "📦 模块状态: 已加载";
    }
}