package com.github.axinger.controller;


import com.github.axinger.entity.User;
import com.github.axinger.service.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/mvc2")
public class UserController2 {
    /// flux 注解方法,和springmvc 类似
    @Autowired
    private UserService userService;

    @SneakyThrows
    @GetMapping("/sleep/{id}")
    public Mono<Map> sleep(@PathVariable Integer id) {
        TimeUnit.SECONDS.sleep(id);
        return Mono.justOrEmpty(Map.of("time", id));
    }


    @GetMapping("/user/{id}")
    public Mono<User> getUserById(@PathVariable Integer id) {

        return userService.getUserById(id);
    }

    // 查询所有用户 Flux 多个返回值
    // @GetMapping(path = "/user/list")
    // @GetMapping(path = "/user/list", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @GetMapping(path = "/user/list", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Object> getAllUser() {
        return userService.getAllUser().delayElements(Duration.ofSeconds(2));
    }

    // 添加用户, 注意 参数类型 Mono<User>
    @PostMapping("/add")
    public Mono<Void> addUser(@RequestBody User user) {
        Mono<User> userMono = Mono.just(user);
        return userService.addUser(userMono);
    }
}
