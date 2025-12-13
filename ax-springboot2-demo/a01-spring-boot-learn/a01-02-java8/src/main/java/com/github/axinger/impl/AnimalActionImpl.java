package com.github.axinger.impl;

import com.github.axinger.AnimalAction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName AnimalActionImpl.java
 * @description TODO
 * @createTime 2022年06月23日 10:00:00
 */

@Slf4j
public class AnimalActionImpl<T> implements AnimalAction<T> {
    @Override
    public T show(T t) {
        return t;
    }

    @SneakyThrows
    @Override
    public T show1(Class<T> tClass) {
        return tClass.newInstance();
    }

    @SneakyThrows
    @Override
    public T show2() {
        T t = null;


        Type genericSuperclass = this.getClass().getGenericSuperclass();
        System.out.println("genericSuperclass = " + genericSuperclass);

        final Class<?>[] interfaces = this.getClass().getInterfaces();
        final Class<?> anInterface = interfaces[0];
        System.out.println("anInterface = " + anInterface);

        System.out.println("anInterface.getAnnotatedInterfaces() = " + anInterface.getAnnotatedInterfaces());
//        final Object instance = anInterface.newInstance();
//        System.out.println("instance = " + instance);

        System.out.println("interfaces[0].getGenericSuperclass() = " + interfaces[0].getGenericSuperclass());


        if (genericSuperclass instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
            for (Type type : actualTypeArguments) {
                System.out.println("type:" + type);
            }
            t = (T) actualTypeArguments[0];
        }
        return t;
    }

//
//    T t = null;
//    Type genericSuperclass = this.getClass().getGenericSuperclass();
//
//        if (genericSuperclass instanceof ParameterizedType) {
//        Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
//        for (Type type : actualTypeArguments) {
//            System.out.println("type:" + type);
//        }
//        t = (T) actualTypeArguments[0];
//    }
//        return t;
}
