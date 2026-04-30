package com.github.axinger.api.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户状态枚举
 */
@Getter
@AllArgsConstructor
public enum UserStatusEnum {

    ACTIVE(1, "正常"),
    DISABLED(0, "禁用"),
    DELETED(-1, "已删除");

    private final Integer code;

    private final String desc;
}
