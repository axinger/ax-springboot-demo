package com.github.axinger;

import com.axing.common.response.dto.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.axinger.model.dto.Person;
import com.github.axinger.model.enums.Gender;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResultTests {

    @Test
    public void test() throws JsonProcessingException {

        Person person = Person.builder()
                .name("小明")
                .age(18)
                .gender(Gender.male)
                .build();

        Result<Person> result = Result.success(person);

        Map<String, Object> params = new HashMap<>();
        params.put("name", "小王");
        result.setParams(params);

        result.setParams("name2", "小王");
        result.setParams("age", "20");

        System.out.println("result = " + result);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(result);
        System.out.println("json = " + json);
    }

    @Test
    public void test2() throws IOException {
        String string = """
                {"code":200,"data":{"id":0,"name":"小明","age":18,"gender":1},"msg":null,"success":true,"name":"小王","name2":"小王","age":"20"}
                """;
        ObjectMapper mapper = new ObjectMapper();

        TypeReference<Result<Person>> typeReference = new TypeReference<>() {
        };
        Result<Person> result = mapper.readValue(string, typeReference);

        System.out.println("result = " + result);
    }
}
