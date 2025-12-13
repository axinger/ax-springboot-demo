package com.github.axinger.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.axinger.bean.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/point")
public class PointController {

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping(value = "/1")
    public Object json(@RequestBody Point point) throws JsonProcessingException {
        System.out.println("point = " + point);


        String json = objectMapper.writeValueAsString(point);
        System.out.println("json = " + json);


        Point reader = objectMapper.readValue(json, Point.class);
        System.out.println("reader = " + reader);

//        JsonParser jsonParser = new JacksonJsonParser();

        return point;
    }
}
