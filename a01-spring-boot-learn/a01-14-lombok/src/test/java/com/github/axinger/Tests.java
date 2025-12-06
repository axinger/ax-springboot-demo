package com.github.axinger;

import com.axing.common.util.json.JsonUtil;
import com.github.axinger.model.GetterSetterExample;
import com.github.axinger.model.Person;
import org.junit.Test;

public class Tests {


    @Test
    public void test1() {

        Person person = new Person();

        person.aFirst("aFirst");


        String string = JsonUtil.toJsonStr(person);
        System.out.println("string = " + string);
    }


    @Test
    public void test2() {

        GetterSetterExample example = new GetterSetterExample();
        example.setAge(1);
//        example.setName(""); //没有set方法
    }


    @Test
    public void test3() {
        Person person = new Person();
        // person.setAddress(null);
        person.setAddress("aaa");
        System.out.println("person = " + person);

    }


}
