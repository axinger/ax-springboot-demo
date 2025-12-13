package com.github.axinger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.axinger.jackson.JacksonUser;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class JacksonController {

    @Autowired
    private ObjectMapper mapper;
    @SneakyThrows
    @GetMapping(value = "/JsonMixin")
    public Object JsonMixin() {
        JacksonUser person = new JacksonUser();
        person.setName("jim");
        person.setAge(10);
        System.out.println("person = " + person);
        return person;
    }

    @SneakyThrows
    @GetMapping(value = "/JsonMixin2")
    public Object JsonMixin2() {
        JacksonUser person = new JacksonUser();
        person.setName("jim");
        person.setAge(10);
        System.out.println("person = " + person);

        String json = mapper.writer()
                .withAttribute("version", "v2.1")
                .withAttribute("traceId", UUID.randomUUID().toString())
                .withAttribute("timestamp", System.currentTimeMillis())
                .writeValueAsString(person);
        return json;
    }
}
