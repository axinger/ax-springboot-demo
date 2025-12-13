package com.github.axinger;

import lombok.*;

@Data
@Builder
@RequiredArgsConstructor(staticName = "of") // 使生成的构造方法是私有的
// 并且生成一个参数为 final 变量和 @NonNull 注解变量，返回类型为当前对象的静态方法，方法名为 staticName 值
// @RequiredArgsConstructor(staticName = "of", access = AccessLevel.PROTECTED, onConstructor_ = {@Deprecated})
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class User {
    private final int age;
    private Integer id;
    private String name;
    private String area;
    private City city;

    public static void main(String[] args) {
        User user = User.of(1);
        System.out.println("user = " + user);
    }
}

