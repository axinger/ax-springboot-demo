package com.github.axinger.controller;

import com.github.axinger.domain.Person;
import com.github.axinger.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {


    @Autowired
    private CacheService cacheService;


    @GetMapping("/put")
    public Person put(Long id) {
        Person person = Person.builder()
                .id(id)
                .name("name-" + id)
                .age(18)
                .build();
        Person person1 = cacheService.put(id, person);
        System.out.println("person1 = " + person1);
        return person1;
    }

    @GetMapping("/get")
    public Person get(Long id) {
        Person person1 = cacheService.get(id);
        System.out.println("person1 = " + person1);
        return person1;
    }

    @GetMapping("/remove")
    public Boolean remove(Long id) {
        return cacheService.remove(id);
    }

    @GetMapping("/getAllKeys")
    public List<Object> getAllKeys(String cacheName) {
        return cacheService.getAllKeys(cacheName);
    }

    @GetMapping("/getAllCacheNames")
    public List<String> getAllCacheNames() {
        return cacheService.getAllCacheNames();
    }

}
