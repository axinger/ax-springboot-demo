package com.github.axinger;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

/**
 * com.fasterxml.jackson.datatype:jackson-datatype-guava 是 Jackson 库的一个扩展模块，
 * 用于支持 Google Guava 库中的数据类型在 JSON 序列化（serialize）和反序列化（deserialize）过程中的处理。
 * ----不用也能解析
 */
public class Guava2JSONTests {

    @SneakyThrows
    @Test
    public void test01() {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new GuavaModule());

        ImmutableList<String> list = ImmutableList.of("a", "b");
        String json = mapper.writeValueAsString(list); // ["a","b"]
        System.out.println("json = " + json);

        ImmutableList<String> deserialized = mapper.readValue(json, new TypeReference<>() {
        });
        System.out.println("deserialized = " + deserialized);


        Multimap<String, Object> map = ArrayListMultimap.create();
        map.put("name", "jim");
        map.put("name", "tom");

        map.put("age", "1");
        map.put("age", "2");

        // map = {name=[jim, tom], age=[1, 2]}
        System.out.println("map = " + map);
        String json2 = mapper.writeValueAsString(map); // {"name":["jim","tom"],"age":["1","2"]}
        System.out.println("json2 = " + json2);

        String jsonString = JSON.toJSONString(map);
        System.out.println("jsonString = " + jsonString);

        Multimap<String, Object> map3 = JSONObject.parseObject(jsonString, new com.alibaba.fastjson2.TypeReference<Multimap<String, Object>>() {
        });
        System.out.println("map3 = " + map3);
    }

}
