package com.github.axinger.model;

public class Views {
    public static class Public {
    }  // 公共视图

    public static class Internal extends Public {
    }  // 内部视图（继承包含公共字段）

    public static class Admin extends Internal {
    }  // 管理员视图
}
