package com.github.axinger.model;

import lombok.ToString;

// @ToString(
//     // 输出内容是否包含字段名，开启时形如: age=10,关闭时: 10
//     includeFieldNames = true,
//     // 需要排除的字段名
//     exclude = {"excludeField"},
//     // 默认使用get函数获取变量值,否则直接拿值,get函数中做数据转换的童鞋注意了
//     doNotUseGetters = false,
//     // 是否把父类的信息也打印出来,默认是不打印
//     callSuper = false,
//     // 开启时只有标注了ToString.Include的字段才会包含
//     onlyExplicitlyIncluded = false
// )
@ToString
public class ToStringExample {

    private int age = 10;

    @ToString.Exclude
    private int excludeField = 0;

    @ToString.Include(
            // 可定义输出顺序
            rank = 1, // 默认0,不生效
            // 可定义名称
            name = ""
    )
    private int includeField = 1;


    public static void main(String[] args) {
        ToStringExample example = new ToStringExample();
        System.out.println("example = " + example);
    }
}
