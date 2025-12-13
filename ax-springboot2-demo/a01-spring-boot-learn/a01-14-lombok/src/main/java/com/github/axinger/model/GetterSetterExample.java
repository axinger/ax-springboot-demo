package com.github.axinger.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * 当标注在类上的时候，为所有的字段生成方法
 */
@Getter
@Setter
public class GetterSetterExample {
    /**
     * 可简单标注在字段上
     */
    @Getter
    @Setter
    private int age = 10;

    /**
     * 可以设置访问级别，默认为public
     * 当置为级别设置为None的时候是不生成方法，当类标注起作用时，这种使用方法可以排除某些字段
     */
    @Setter(AccessLevel.PROTECTED)  // 内部调用可以set,外部调用不可以set
    private String name;
}

