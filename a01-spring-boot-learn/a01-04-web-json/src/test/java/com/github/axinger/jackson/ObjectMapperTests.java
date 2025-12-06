package com.github.axinger.jackson;

import com.axing.common.util.json.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.github.axinger.entity.Person2;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;


public class ObjectMapperTests {


    @Test
    public void test_21() throws JsonProcessingException {

        TypeReference<Person2> typeReference = new TypeReference<>() {
        };


        String json = """
                {
                "id":"123",
                "name":"jim",
                "age":20
                }
                """;


        // 使用 Jackson 解析泛型
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Person2 person2 = mapper.readValue(json, typeReference);
        System.out.println("person2 = " + person2);

    }

    @Test
    public void test_01() throws JsonProcessingException {
        {


            String json = """
                    {"user":{"name":"Alice"}}
                    """;
            ObjectMapper mapper = JsonUtil.mapper;
            mapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
            SysUser sysUser = mapper.readValue(json, SysUser.class);
            System.out.println("root name = " + sysUser);
            /// 用后移除一下
            mapper.disable(DeserializationFeature.UNWRAP_ROOT_VALUE);

            SysUser sysUser2 = new SysUser();
            sysUser2.setName("tom");
            mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
            String json2 = mapper.writeValueAsString(sysUser2);
            System.out.println("json2 = " + json2);
            /// 用后移除一下
            mapper.disable(SerializationFeature.WRAP_ROOT_VALUE);
        }

        {
            TypeReference<SysUser> typeReference = new TypeReference<>() {
            };

            String json = """
                    {
                    "id":"123",
                    "name":"jim",
                    "age":20
                    }
                    """;

            // 使用 Jackson 解析泛型
//            JsonUtil.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            SysUser sysUser = JsonUtil.toBean(json, typeReference);
            System.out.println("sysUser = " + sysUser);
        }


        {
            String json = """
                    {
                    "id":"123",
                    "username":"jim",
                    "lastName":"li",
                    "age":20,
                    "gender":"5"
                    }
                    """;
            SysUser sysUser = JsonUtil.toBean(json, SysUser.class);

            if (sysUser != null) {
                System.out.println("username 别名 = " + sysUser);
                sysUser.setCreateTime(LocalDateTime.now());
//                sysUser.setUpdateTime(LocalDateTime.now());
                String json2 = JsonUtil.toJsonStr(sysUser);
                System.out.println("json2 = " + json2);
                String json3 = JsonUtil.mapper.writeValueAsString(sysUser);
                System.out.println("json3 = " + json3);
            }

        }


    }

    @Test
    public void test_05() throws JsonProcessingException {

        /*

        {
  "id" : "123",
  "name" : "jim",
  "lastName" : "li",
  "gender" : 0,
  "createTime" : "2025-11-05 18:20:13",
  "updateTime" : "2025-11-05 18:20:13",
  "data" : {"id":123,"valid":true},   就不会转义了   {\"id\":123,\"valid\":true}
  "full_name" : "jim li",
  "age" : 20
}
         */


        {
            String json = """
                    {
                    "id":"123",
                    "username":"jim",
                    "lastName":"li",
                    "age":20,
                    "gender":"5"
                    }
                    """;
            SysUser sysUser = JsonUtil.toBean(json, SysUser.class);
            System.out.println("username 别名 = " + sysUser);

            sysUser.setCreateTime(LocalDateTime.now());
            sysUser.setUpdateTime(LocalDateTime.now());

            // 方式1：直接使用 JsonNodeFactory
//            ObjectNode jsonNodes = JsonNodeFactory.instance.objectNode();
            ObjectNode jsonNodes = JsonUtil.mapper.createObjectNode();
            jsonNodes.put("id", 123);
            jsonNodes.put("valid", true);

            String string = jsonNodes.toString();
//            String string = jsonNodes.toPrettyString();
            System.out.println("string = " + string);

//            sysUser.setData("{\"id\":123,\"valid\":true}");
            sysUser.setData(string);
            sysUser.setData2(string);

            sysUser.setOtherProperty("zip", "100000");
            sysUser.setOtherProperty("city", "Beijing");
            sysUser.setOtherProperty("addr", "XXX St");

            String json2 = JsonUtil.toJsonStr(sysUser);
            System.out.println("json2 = " + json2);
        }
    }

    /**
     * @JsonMerge 用途：在反序列化时 合并 JSON 数据到已有对象（而非完全覆盖），适用于部分更新（PATCH）场景
     */
    @Test
    public void test_06() throws JsonProcessingException {

        SysUser original = new SysUser();
        original.settings.put("theme", "dark");

        ObjectMapper mapper = JsonUtil.mapper;
        mapper.setDefaultMergeable(true); // 或在字段上用 @JsonMerge

        String patchJson = "{\"settings\":{\"lang\":\"en\"}}";
        SysUser updated = mapper.readerForUpdating(original).readValue(patchJson);

        System.out.println("updated = " + updated);
        System.out.println("original = " + original);


// updated.settings = {"theme":"dark", "lang":"en"}
    }

    @Test
    public void test_07() {

        String json = """
                [
                    {
                        "id": "123",
                        "name": "jim",
                        "age": 20
                    }
                ]
                """;
        List<SysUser> list = JsonUtil.toBeanList(json, SysUser.class);
        System.out.println("list = " + list);
    }

    /// 常用于按权限/场景动态决定返回哪些字段。
    @Test
    public void test_02() throws JsonProcessingException {

        SysUser user = new SysUser();
        user.setId("1");
        user.setName("tom");

        ObjectMapper mapper = new ObjectMapper();
        FilterProvider filters = new SimpleFilterProvider()
                .addFilter("userFilter", SimpleBeanPropertyFilter.filterOutAllExcept("name"));
        String json = mapper.writer(filters).writeValueAsString(user);
        System.out.println("json = " + json);
    }

    @Test
    public void test_03() {


        {
            String json = """
                    { "type": "dog", "breed": "Golden" }
                    """;
            Animal animal = JsonUtil.toBean(json, Animal.class);
            System.out.println("animal = " + animal);
            System.out.println("animalgetClass = " + animal.getClass());
        }

        {
            String json = """
                    { "type": "cat", "lazy": 1 }
                    """;
            Animal animal = JsonUtil.toBean(json, Animal.class);
            System.out.println("animal = " + animal);
            System.out.println("animalge tClass = " + animal.getClass());
        }

    }

    @Test
    public void test_04() {
//        InjectableValues inject = new InjectableValues.Std()
//                .addValue("dataSource", myDataSource);
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.reader(inject).forType(ServiceConfig.class).readValue(json);
    }
}
