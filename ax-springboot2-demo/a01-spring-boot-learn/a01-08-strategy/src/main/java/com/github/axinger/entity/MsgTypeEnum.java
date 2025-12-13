package com.github.axinger.entity;

public enum MsgTypeEnum {

    TEXT(1, "文本"),
    IMAGE(2, "图片"),
    VIDEO(3, "视频");

    public final int code;
    public final String name;

    MsgTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
