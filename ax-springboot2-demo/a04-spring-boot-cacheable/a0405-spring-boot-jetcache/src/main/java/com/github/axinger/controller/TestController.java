package com.github.axinger.controller;

import com.github.axinger.model.User;
import com.github.axinger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserService userService;

    @GetMapping("/1")
    public User getProduct(@RequestParam("id") Long id) {
        return userService.getUserById(id);
    }


//    @Autowired
//    private CacheManager cacheManager;
//
//    private Cache<String, Object> userCache;
//    @GetMapping("/2")
//  public void   test2() {
//        userCache.tryLockAndRun("key", 60, TimeUnit.SECONDS, () -> {
//
//        });
//    }
}
