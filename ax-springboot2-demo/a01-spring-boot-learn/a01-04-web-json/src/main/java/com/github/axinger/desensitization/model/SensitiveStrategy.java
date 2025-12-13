package com.github.axinger.desensitization.model;

import java.util.function.Function;

public enum SensitiveStrategy {
    // 对于用户名脱敏操作
    USERNAME(s -> s.replaceAll("(\\S)\\S(\\S*)", "$1*$2")),
    // 对用户身份证号进行脱敏
    ID_CARD(s -> s.replaceAll("(\\d{4})\\d{10}(\\w{4})", "$1****$2")),
    // 对用户手机号进行脱敏
    PHONE(s -> s.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2")),
    // 对地址进行脱敏
    ADDRESS(s -> s.replaceAll("(\\S{3})\\S{2}(\\S*)\\S{2}", "$1****$2****"));

    private final Function<String, String> desensitizer;

    SensitiveStrategy(Function<String, String> desensitizer) {
        this.desensitizer = desensitizer;
    }

    public Function<String, String> desensitizer() {
        return desensitizer;
    }
}
