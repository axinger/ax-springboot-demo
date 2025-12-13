package com.github.axinger.xml.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;


class Tests {


    /**
     * xml 读取属性
     */
    @Test
    void test_person() {
        System.out.println("test1........");

        ApplicationContext context = new ClassPathXmlApplicationContext("bean1.xml");
        Person person = context.getBean("person", Person.class);
        System.out.println("6.bean可以使用了(对象获取到了);");
        System.out.println("person = " + person);
        person.add();

        // 手动调用销毁
        ((ClassPathXmlApplicationContext) context).close();

    }

    @Test
    void test_person2() {
        System.out.println("test1........");

        BeanFactory beanFactory = new ClassPathXmlApplicationContext("bean2.xml");
        Person person1 = beanFactory.getBean("person", Person.class);
        System.out.println("person1 = " + person1);

        person1.add();

        Person person2 = beanFactory.getBean("person", Person.class);
        System.out.println("person2 = " + person2);

    }

    @Test
    void test_person_factory() {
        System.out.println("test1........");

        BeanFactory beanFactory = new ClassPathXmlApplicationContext("bean3.xml");
        Person person1 = beanFactory.getBean("person", Person.class);
        System.out.println("person1 = " + person1);

        Person person2 = beanFactory.getBean("person", Person.class);
        System.out.println("person2 = " + person2);

    }

    @Test
    /**
     * xml 读取属性
     * */
    void test1() {
        System.out.println("test1........");

        BeanFactory beanFactory = new ClassPathXmlApplicationContext("bean1.xml");
        User user = beanFactory.getBean("user", User.class);
        System.out.println("user = " + user);

        user.add();

    }


    @Test
    void test_person_auto() {

        ApplicationContext context = new ClassPathXmlApplicationContext("bean4_auto.xml");
        Person person = context.getBean("person", Person.class);
        person.getIdCard().setName("id_name");
        System.out.println("person.getIdCard() = " + person.getIdCard());
        System.out.println("person = " + person);

    }


    @Test
    void testLog() {
        ArrayList arrayList = new ArrayList<String>() {
            {
                add("aa");
            }
        };
        System.out.println("arrayList = " + arrayList);

        String[] strs = new String[]{"a", "b"};
        System.out.println("strs = " + Arrays.toString(strs));
    }


}
