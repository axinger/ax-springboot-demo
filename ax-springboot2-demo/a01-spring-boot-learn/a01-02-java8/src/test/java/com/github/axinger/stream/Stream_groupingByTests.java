package com.github.axinger.stream;

import com.alibaba.fastjson2.JSON;
import com.github.axinger.Person;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Stream_groupingByTests {

    static List<Person> personList = Person.personList;

    @Test
    void test_分组list() {


        Map<String, List<String>> collect = personList.stream()
                .collect(Collectors.groupingBy(Person::getSex,
                        Collectors.mapping(Person::getName, Collectors.toList())
                ));
        //{女=[赵六], 男=[张三, 李四, 王五]}
        System.out.println("collect = " + collect);

        //排序后分组
        LinkedHashMap<String, List<Person>> collect3 = personList.stream()
                .sorted(Comparator.comparing(Person::getAge))
                .collect(Collectors.groupingBy(Person::getSex, LinkedHashMap::new, Collectors.toList()));
       /*
{
    "男": [
        {
            "address": "安徽",
            "age": 20,
            "id": 1,
            "name": "张三",
            "sex": "男"
        },
        {
            "address": "江苏",
            "age": 20,
            "id": 3,
            "name": "王五",
            "sex": "男"
        },
        {
            "address": "安徽",
            "age": 21,
            "id": 2,
            "name": "李四",
            "sex": "男"
        }
    ],
    "女": [
        {
            "address": "上海",
            "age": 24,
            "id": 4,
            "name": "赵六",
            "sex": "女"
        }
    ]
}

        */
        System.out.println("collect3 = " + JSON.toJSONString(collect3));
    }

}
