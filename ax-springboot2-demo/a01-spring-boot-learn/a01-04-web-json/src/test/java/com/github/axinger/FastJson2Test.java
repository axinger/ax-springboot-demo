package com.github.axinger;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.axinger.dto.Gender;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FastJson2Test {

    static Person person;

    @BeforeAll
    public static void before() {
        person = Person.builder()
                .id("1")
                .name("jin")
                .age(10)
                .books(List.of(

                        Book.builder()
                                .id("1")
                                .name("西游记")
                                .build(),

                        Book.builder()
                                .id("2")
                                .name("水浒传")
                                .build()
                ))
                .build();
    }

    @Test
    void test1() throws JsonProcessingException {

        Person person = Person.builder()
                .id("1")
                .name("jin")
                .gender(Gender.MALE)
                .books(List.of(

                        Book.builder()
                                .id("1")
                                .name("西游记")
                                .build(),

                        Book.builder()
                                .id("2")
                                .name("水浒传")
                                .build()
                ))
                .build();

        String jsonString = JSON.toJSONString(person);
        System.out.println("jsonString = " + jsonString);

        TypeReference<Map<String, Object>> typeReference = new TypeReference<>() {
        };
        Map<String, Object> map = JSON.parseObject(jsonString, typeReference);
        System.out.println("转泛型map = " + map);

        JSONObject jsonObject1 = JSONObject.from(person);
        System.out.println("jsonObject1 = " + jsonObject1);


        JSONObject jsonObject = JSONObject.from(map);
        System.out.println("jsonObject = " + jsonObject);

        String string = """
                {"age":0,"books":[{"id":"1","name":"西游记"},{"id":"2","name":"水浒传"}],"gender":"男","id":"1","name":"jin"}
                """;
        Person person1 = JSON.parseObject(string, Person.class);
        System.out.println("person1 = " + person1);
        System.out.println("person1.getGender = " + person1.getGender());
        System.out.println("person1.getGender = " + person1.getGender().getCode());
        System.out.println("person1.getGender = " + person1.getGender().getDesc());


//        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(person), JSONObject.class);

        System.out.println("===================================");
        String book1Name = jsonObject.getJSONArray("books").getJSONObject(1).getString("name");
        System.out.println("book1Name = " + book1Name);
        jsonObject.getJSONArray("books").getJSONObject(1).put("name", "tom");
        System.out.println("jsonObject = " + jsonObject);
        System.out.println("map = " + map);
    }


    @Test
    void test_JSONObject() {
        Person person = Person.builder()
                .id("1")
                .name("jin")
                .books(List.of(
                        Book.builder()
                                .id("1")
                                .name("西游记")
                                .build(),
                        Book.builder()
                                .id("2")
                                .name("水浒传")
                                .build()
                ))
                .build();
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(person), new TypeReference<>() {
        });
        JSONObject jsonObject = new JSONObject(map);
        jsonObject.put("name", "修改用户名");
        jsonObject.getJSONArray("books").getJSONObject(1).put("name", "修改书名");
        System.out.println("jsonObject2 = " + jsonObject);
    }

    @Test
    void test_JSONObject2() {

        Person person = Person.builder()
                .id("1")
                .name("jin")
                .books(List.of(
                        Book.builder()
                                .id("1")
                                .name("西游记")
                                .build(),
                        Book.builder()
                                .id("2")
                                .name("水浒传")
                                .build()
                ))
                .build();

        JSONObject jsonObject = JSONObject.from(person);
        jsonObject.put("name", "修改用户名");
        jsonObject.getJSONArray("books").getJSONObject(1).put("name", "修改书名");
        System.out.println("JSONObject.from = " + jsonObject);

    }

    @Test
    void test3() throws JsonProcessingException {


        Person person = Person.builder()
                .id("1")
                .name("jin")
                .books(List.of(

                        Book.builder()
                                .id("1")
                                .name("西游记")
                                .build(),

                        Book.builder()
                                .id("2")
                                .name("水浒传")
                                .build()
                ))
                .build();


        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(person);

        JsonNode jsonNode = objectMapper.readTree(s);


        System.out.println("jsonNode.get(\"name\").asText() = " + jsonNode.get("name").asText());

        System.out.println("jsonNode.path(\"name\").asText() = " + jsonNode.path("name").asText());


    }


    @Test
    void test4() {
        System.out.println("person = " + person);
        String jsonString = JSONObject.toJSONString(person);
        System.out.println("jsonString = " + jsonString);

        Person person1 = JSONObject.parseObject(jsonString).to(Person.class);
        System.out.println("person1 = " + person1);
    }


    @Test
    void test10() {

        // 不可变集合,不能被修改
        Map<String, Object> map = new HashMap<>();
        map.put("name", "jim");
//        JSONObject from = JSONObject.from(map);
        JSONObject from = new JSONObject(map);
        map.put("name", "tom");
        System.out.println("from = " + from);
        System.out.println("map = " + map);
    }
}
