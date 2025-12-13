package com.github.axinger;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;

public class 继承Tests {

    public void func1(Animal animal) {
        Class<? extends Animal> aClass = animal.getClass();
        System.out.println("aClass = " + aClass);
    }

    public void func2(Class<? extends Animal> aClass) {


        Animal animal = JSONObject.parseObject("""
                {
                "name":"jim"
                }
                """, aClass);

        System.out.println("json = " + animal);

        Class<?> show = animal.show();
        System.out.println("show = " + show);

    }

    @SneakyThrows
    public <T extends Animal> Class<?> func3(Class<T> aClass) {

        // 获取 CONFIG
        String config = Animal.CONFIG;
        System.out.println("Animal.CONFIG = " + config);


        Field configField = aClass.getField("CONFIG");
        String config2 = (String) configField.get(null); // 静态字段传入null
        System.out.println("子CONFIG = " + config2);

        // 先获取 flag（在创建实例之前）
        AnimalFlag annotation = aClass.getAnnotation(AnimalFlag.class);
        if (annotation != null) {
            String flagValue = annotation.value();
            System.out.println("Flag value: " + flagValue);
        } else {
            System.out.println("No flag annotation found");
        }

//        Method flagMethod = aClass.getMethod("getFlag");
//        String flagValue = (String) flagMethod.invoke(null);
//        System.out.println("Flag value: " + flagValue);

//        String flag = Animal.flag();
//        System.out.println("flag = " + flag);


        T animal = JSONObject.parseObject("""
                {
                "name":"jim"
                }
                """, aClass);

        System.out.println("func3 json = " + animal);

        Class<?> show = animal.show();
        System.out.println("func3 show = " + show);

        return show;
    }

    public Class<?> func4(Class<? extends Animal> aClass) {
        Animal animal = JSONObject.parseObject("""
                {
                "name":"jim"
                }
                """, aClass);

        System.out.println("func4 json = " + animal);

        Class<?> show = animal.show();
        System.out.println("func4 show = " + show);

        return show; // 返回 show 的结果
    }

    @Test
    public void test1() throws Exception {
        Dog dog = new Dog();
        System.out.println(dog.show());
    }

    @Test
    public void test2() throws Exception {
        func1(new Dog());
    }

    @Test
    public void test3() throws Exception {
        func2(Dog.class);
    }

    @Test
    public void test4() throws Exception {


//        func3(Cat.class); // 不可以
        func3(Dog.class);
//        func4(Cat.class);// 不可以
//        func4(Dog.class);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface AnimalFlag {
        String value();
    }

    interface Animal {

        String CONFIG = "config";

        default String flag() {
            return "A";
        }

        String getFlag(); // 改为实例方法


        default Class<?> show() {
            return this.getClass();
        }
    }

    interface Animal3 {
        String DOG_FLAG = "DOG_FLAG";
    }

    //    @AnimalFlag("DOG_FLAG")
    @AnimalFlag(Animal3.DOG_FLAG)
    @Data
    class Dog implements Animal {

        public static final String CONFIG = "DOG_FLAG";

        private String name;


        @Override
        public String flag() {
            return "B";
        }

        @Override
        public String getFlag() {
            return "B";
        }


    }

    @Data
    class Cat {

        private String name;
    }
}
