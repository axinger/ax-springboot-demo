package com.github.axinger.model.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Java 16 新特性：record类
 * record申明的类，具备这些特点：
 * 因为record关键词申明类主要是为了简化一些类的申明，所以它本质就是一类特殊的class，或者说是某一个模版的class。
 * 它是一个final类
 * 自动实现equals、hashCode、toString函数
 * 成员变量均为public属性
 *
 * @param username
 * @param password
 */

//@EnableConfigurationProperties(UserProperties.class) //配合使用,不能在record 使用,必须在class中引入

@ConfigurationProperties(prefix = "axinger.user")
public record AxingerUserProperties(String username, String password, Dog dog) {

    public String all() {
        return username + password;
    }

    public record range(int min, int max) {
    }

    public record Dog(int min, int max) {
    }

}
