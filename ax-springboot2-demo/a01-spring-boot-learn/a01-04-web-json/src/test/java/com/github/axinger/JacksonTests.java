package com.github.axinger;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.axinger.jackson.MorePerson;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class JacksonTests {

    @SneakyThrows
    @Test
    public void test1() {
        ObjectMapper mapper = new ObjectMapper();
        // 反序列化：JSON -> Java对象
        String json = """
                {
                    "id": "1",
                    "name": "Alice",
                    "gender": "female",
                    "city": "New York",
                    "age": 30
                }
                """;
        MorePerson morePerson = mapper.readValue(json, MorePerson.class);
        System.out.println(morePerson); // 输出: MorePerson(id=1, name=Alice, otherProperties={age=30, city=New York})

        // 序列化：Java对象 -> JSON
        morePerson.setOtherProperty("gender", "female");
        String outputJson = mapper.writeValueAsString(morePerson);
        System.out.println(outputJson); // 输出: {"id":"1","name":"Alice","age":30,"city":"New York","gender":"female"}
    }


    //fastjson2
    @SneakyThrows
    @Test
    public void test2() {
        // 反序列化：JSON -> Java对象
        String json = """
                {
                    "id": "1",
                    "name": "Alice",
                    "gender": "female",
                    "city": "New York",
                    "age": 30
                }
                """;
        MorePerson morePerson = JSON.parseObject(json, MorePerson.class);
        System.out.println(morePerson); // 输出: MorePerson(id=1, name=Alice, otherProperties={age=30, city=New York})

        // 序列化：Java对象 -> JSON

        String jsonString = JSON.toJSONString(morePerson);
        System.out.println(jsonString); // 输出: {"id":"1","name":"Alice","age":30,"city":"New York","gender":"female"}
    }


    //fastjson1
    @SneakyThrows
    @Test
    public void test3() {
        // 反序列化：JSON -> Java对象
        String json = """
                {
                    "id": "1",
                    "name": "Alice",
                    "gender": "female",
                    "city": "New York",
                    "age": 30
                }
                """;
        MorePerson morePerson = com.alibaba.fastjson.JSON.parseObject(json, MorePerson.class);
        System.out.println(morePerson); // 输出: MorePerson(id=1, name=Alice, otherProperties={age=30, city=New York})

        // 序列化：Java对象 -> JSON

        String jsonString = com.alibaba.fastjson.JSON.toJSONString(morePerson);
        System.out.println(jsonString); // 输出: {"id":"1","name":"Alice","age":30,"city":"New York","gender":"female"}
    }
}
