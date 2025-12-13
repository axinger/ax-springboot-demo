package com.github.axinger.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Data
/*
 * 生成静态方法,只有 @NonNull 注解的,指定参数的静态构造方法
 */
@RequiredArgsConstructor(staticName = "of")
//@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class Address {

    @NonNull
    private String country;

    @NonNull // lombok.NonNull, 可以校验,初始化
    private String city;

    private Area area;


    private String houseNumber;

    private String street;

}
