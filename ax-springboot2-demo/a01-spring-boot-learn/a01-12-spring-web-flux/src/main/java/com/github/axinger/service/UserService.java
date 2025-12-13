package com.github.axinger.service;

import com.github.axinger.entity.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

    // 根据id查询用户 , Mono 返回0或1 个返回值
    Mono<User> getUserById(Integer id);

    // 查询所有用户 Flux 多个返回值
    Flux<Object> getAllUser();


    // 添加用户, 注意 参数类型 Mono<User>
    Mono<Void> addUser(Mono<User> userMono);
}
