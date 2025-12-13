package com.github.axinger.model;

import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ConstructorExample<T> {
    private int x, y;
    @NonNull
    private T description;

    public static void main(String[] args) {
        ConstructorExample example = new ConstructorExample(1, 2, "2");

        // 无参构造,静态方法
        NoArgsExample of = NoArgsExample.of();
        NoArgsExample of2 = new NoArgsExample();
    }

    @NoArgsConstructor(
            // 如果不为空，会生成一个静态的无参构造函数
            staticName = "of",
            // 该参数允许在生成的构造函数上添加指定的注解，但是不稳定，慎用
            onConstructor = {},
            // 可以控制访问权限
            access = AccessLevel.PUBLIC,
            // 当字段中有@NonNull标识的字段时,理论上需要外部传入数据来初始化
            // force为true时会强制使用0/false/null来初始化这些字段
            force = false
    )
    public static class NoArgsExample {
        @NonNull
        private String field;
    }
}
