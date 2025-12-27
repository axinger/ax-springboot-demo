package com.github.axinger.controller;

import com.github.axinger.model.dto.Person;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/enum")
public class EnumController {

    /**
     * 枚举作为参数 post请求,走json序列化 @JsonCreator 就可以
     *
     * @param person person
     * @return Object
     */
    @PostMapping("/test1")
    public Object test1(@RequestBody Person person) {
        System.out.println("枚举作为参数 = " + person);
        return Map.of("枚举post参数", person);
    }

    @GetMapping("/test2")
    public Object test2(@ModelAttribute Person person) {
        System.out.println("枚举作为参数 = " + person);
        return Map.of("枚举get参数", person);
    }

}
