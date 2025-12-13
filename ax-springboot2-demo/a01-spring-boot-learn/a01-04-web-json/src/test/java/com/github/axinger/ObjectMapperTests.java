package com.github.axinger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.axinger.entity.Person2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class ObjectMapperTests {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void test_01() throws JsonProcessingException {

        TypeReference<Person2> typeReference = new TypeReference<>() {
        };


        String json = """
                {
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
}
