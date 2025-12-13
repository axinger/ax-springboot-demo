package com.github.axinger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//public class Person implements Comparable {
public class Person {

    /// /            return  Comparator.comparing(Person::getName).thenComparing(Person::getAge);
//
//        }
//        throw new RuntimeException("类型不匹配");
//    }

    public static List<Person> personList = new ArrayList<>() {{
        add(Person.builder().id(1).name("张三").sex("男").age(20).address("安徽").build());
        add(Person.builder().id(2).name("李四").sex("男").age(21).address("安徽").build());
        add(Person.builder().id(3).name("王五").sex("男").age(20).address("江苏").build());
        add(Person.builder().id(4).name("赵六").sex("女").age(24).address("上海").build());
    }};
    private Integer id;
    private String name;
    private String sex;
    private Integer age;


//    @Override
//    public int compareTo(Object o) {
//        if (o instanceof Person p) {
//            int sort;
//            sort = name.compareTo(p.name);
    /// /            if (sort == 0) {
    /// /                sort = Integer.compare(age, p.age);
    /// ///                sort = age.compareTo(p.age);
    /// /            }
//            return sort;
    private String address;


    private Map<String, Object> params;


    public Person(Integer id, String name, String sex, Integer age, String address) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.address = address;
    }

}
