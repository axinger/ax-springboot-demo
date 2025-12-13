package com.github.axinger.controller;

import com.github.axinger.model.dto.Person;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class EnumController {

    /**
     * 枚举作为参数
     *
     * @param person person
     * @return Object
     */
    @PostMapping("/gender")
    public Object gender(@RequestBody Person person) {
        System.out.println("枚举作为参数 = " + person);
        return Map.of("枚举参数", person);
    }

}
