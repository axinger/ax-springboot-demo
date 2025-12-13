package com.github.axinger;

import lombok.Data;
import lombok.ToString;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName Dog.java
 * @description TODO
 * @createTime 2022年06月23日 10:27:00
 */

@Data
@ToString(callSuper = true)
public class Dog extends Animal {


    @Override
    void eat() {

    }
}
