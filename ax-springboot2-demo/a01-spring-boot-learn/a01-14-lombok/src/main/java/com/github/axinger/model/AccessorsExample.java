package com.github.axinger.model;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.Delegate;
// 2. @Accessors 属性说明
//2.1 fluent 属性
//
//    不写默认为false，当该值为 true 时，对应字段的 getter 方法前面就没有 get，setter 方法就不会有 set。

//2.2 chain 属性
//
//    不写默认为false，当该值为 true 时，对应字段的 setter 方法调用后，会返回当前对象。
//2.3 prefix 属性
//
//    该属性是一个字符串数组，当该数组有值时，表示忽略字段中对应的前缀，生成对应的 getter 和 setter 方法。

// fluent,chain 同时存在 chain 无效
@Accessors(
        // getAge()-> age(), setAge(int age)-> age(int age)
        fluent = true,
        // void setAge(int age)   ->  AccessorsExample setAge(int age), 就可以连续调用了
        chain = true
)

@Data
public class AccessorsExample {
    private int age;
    private String name;

    // 忽略前缀
    @Accessors(prefix = "f")
    @Delegate
    private String fId;

    public static void main(String[] args) {
        AccessorsExample example = new AccessorsExample();
        example.age(10).name("jim").id("1");
        System.out.println("example.age = " + example.age);
        System.out.println("example.name = " + example.name);

//        AccessorsExample age1 = example.age(10);
//        AccessorsExample example1 = example.setAge(10);


        System.out.println("example = " + example);
    }
}




