package com.github.axinger;


import com.github.axinger.controller.UserController;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.constraints.NotEmpty;
import java.lang.annotation.Annotation;
import java.util.Map;

class MainTest {

    // 获取指定类上的注解信息：
    @Test
    void test1() {
        Annotation annotation = AnnotationUtils.findAnnotation(UserController.class, RequestMapping.class);
        System.out.println("annotation = " + annotation);
    }


    // 获取指定方法上的注解信息：
    @SneakyThrows
    @Test
    void test2() {
        //方法public
        // RequestMapping不兼容 GetMapping
        Annotation annotation = AnnotationUtils.findAnnotation(UserController.class.getMethod("list"), GetMapping.class);
        System.out.println("annotation = " + annotation);
    }

    //获取指定字段上的注解信息：
    @SneakyThrows
    @Test
    void test3() {
        //字段也要public
        Annotation annotation = AnnotationUtils.findAnnotation(UserController.class.getField("name"), NotEmpty.class);
        System.out.println("annotation = " + annotation);
    }

    //检查类、方法或字段上是否存在指定注解：
    @SneakyThrows
    @Test
    void test4() {
        //失效,判断 Annotation annotation = AnnotationUtils.findAnnotation   annotation ==null

//        boolean hasAnnotation = AnnotationUtils.hasAnnotation(YourClass.class, YourAnnotation.class);
    }


    //获取指定注解上的属性值：
    @SneakyThrows
    @Test
    void test5() {
        Annotation annotation = AnnotationUtils.findAnnotation(UserController.class.getField("name"), NotEmpty.class);
        Object message = AnnotationUtils.getValue(annotation, "message");
        System.out.println("message = " + message);

    }

    //获取注解上所有属性的名称和属性值：
    @SneakyThrows
    @Test
    void test6() {
        Annotation annotation = AnnotationUtils.findAnnotation(UserController.class.getField("name"), NotEmpty.class);
        Map<String, Object> attributes = AnnotationUtils.getAnnotationAttributes(annotation);
        System.out.println("attributes = " + attributes);
    }
}
