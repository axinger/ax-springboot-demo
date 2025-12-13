package com.github.axinger.handler;

import com.github.axinger.entity.User;
import com.github.axinger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserHandler {

    @Autowired
    private UserService userService;

    // public Mono<ServerResponse> getUserById(@PathVariable(name = "id") Integer id) {

    public Mono<ServerResponse> getUserById(ServerRequest request) {

        // 获取参数
        Integer id = Integer.valueOf(request.pathVariable("id"));

        System.out.println("id = " + id);

        /// 空值处理
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        Mono<User> userMono = userService.getUserById(id);
        System.out.println("userMono = " + userMono);
        return userMono.flatMap(e -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
//                .body(fromObject(e))
                .body(userMono, User.class) /// user 需要无参构造
                .switchIfEmpty(notFound));
    }


    public Mono<ServerResponse> getAllUser(ServerRequest request) {

        Flux<Object> allUser = userService.getAllUser();

        return ServerResponse.ok().body(BodyInserters.fromValue(allUser));

//        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(allUser, User.class);
    }


    public Mono<ServerResponse> addUser(ServerRequest request) {
        Mono<User> userMono = request.bodyToMono(User.class);
        return ServerResponse.ok().build(userService.addUser(userMono));
    }


}
