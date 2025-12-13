package com.github.axinger.enums;

public enum Week {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY;

    /// 枚举是线程安全的单例
    public void print() {
        System.out.println("我的名字" + this.name());
    }

    public boolean isWeekEnd() {
        return this == SATURDAY || this == SUNDAY;
    }
}


