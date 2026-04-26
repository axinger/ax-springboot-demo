package com.github.axinger.groovy;

public enum SecurityStrategy {
    OPEN,       // 开放模式 (默认，类似 QLExpress 的开放策略)
    WHITE_LIST, // 白名单模式 (类似 QLExpress 的白名单)
    SANDBOX     // 沙箱模式 (更严格的限制)
}