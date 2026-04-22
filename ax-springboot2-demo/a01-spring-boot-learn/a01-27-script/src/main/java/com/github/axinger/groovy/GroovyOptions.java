package com.github.axinger.groovy;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroovyOptions {
    /**
     * 是否开启精确计算模式
     * 注意：Groovy 对 0.1 这种字面量默认精确，但对 Double 变量不精确。
     * 开启此开关建议在业务层确保传入的是 BigDecimal 类型。
     */
    @Builder.Default
    private boolean precise = false;

    /**
     * 安全策略
     */
    @Builder.Default
    private SecurityStrategy securityStrategy = SecurityStrategy.OPEN;
}

enum SecurityStrategy {
    OPEN,       // 开放模式 (默认，类似 QLExpress 的开放策略)
    WHITE_LIST, // 白名单模式 (类似 QLExpress 的白名单)
    SANDBOX     // 沙箱模式 (更严格的限制)
}