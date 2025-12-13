package com.github.axinger.model;

// 可以将sealed修饰符应用到接口的声明中。然后，permit子句指定允许实现密闭接口的类：
// 我们可以通过使用相同的sealed修饰符来定义密闭类：
//  public abstract sealed class Vehicle permits Car, Truck {     }
public sealed interface Animal permits Dog {

}
