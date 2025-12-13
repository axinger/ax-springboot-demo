package com.github.axinger;

import org.junit.jupiter.api.Test;


public class StaticTest {

    void test2(StaticPerson person) {
        person.name3 = "name3.........";

    }


    @Test
    public void test1() {

        // final 变量无法修改
//        Person.name = "";
        System.out.println("Person.name = " + StaticPerson.name);
        // static 变量可以被修改
        StaticPerson.name2 = "jim1";
        System.out.println("Person.name2 = " + StaticPerson.name2);

        // 局部变量,不能再次赋值
        final StaticPerson person = new StaticPerson();
        System.out.println("实例对象访问静态变量 person = " + person.name2);

        person.name2 = "jim2";

        final StaticPerson person1 = new StaticPerson();
        System.out.println("实例对象访问静态变量 person1 = " + person1.name2);

        person1.name3 = "name3";

        test2(person1);
        System.out.println("引用传值 name3 = " + person1.name3);

    }


    @Test
    // 在 Java 中，如何跳出当前的多重嵌套循环
    public void test2() {

        // 在Java中，要想跳出多重循环，可以在外面的循环语句前定义一个标号
        ok:
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.println("i=" + i + ",j=" + j);
                if (j == 5) {
                    System.out.println("break ok");
                    break ok;
                }
            }
        }
        System.out.println("跳出多层for");
    }
}
