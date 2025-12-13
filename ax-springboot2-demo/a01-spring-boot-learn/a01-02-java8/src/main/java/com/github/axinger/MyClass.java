package com.github.axinger;

import lombok.Data;

@Data
public class MyClass<T> {
    // 非静态方法，可以使用类的泛型参数 T
    private T value;

    // 静态方法使用自己的泛型参数 U
    public static <U> U staticMethod(U param) {
        System.out.println("Static method parameter: " + param);
        return param;
    }
}

class Main {
    public static void main(String[] args) {
        MyClass<Integer> myClass = new MyClass<>();
        myClass.setValue(42);

        Integer value = myClass.getValue();
        System.out.println("Value: " + value);

        // 静态方法调用，不依赖于类的实例
        String hello = MyClass.staticMethod("Hello");
        Double v = MyClass.staticMethod(3.14);
    }
}
