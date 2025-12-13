package com.github.axinger.controller;

import com.github.axinger.handler.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
@RestController
public class RouteConfig {
    @Bean
    public RouterFunction<ServerResponse> user1(UserHandler userHandler) {
        return RouterFunctions
                .route(GET("/flux/users/list").and(accept(MediaType.APPLICATION_JSON)), userHandler::getAllUser)
                .andRoute(GET("/flux/user/{id}").and(accept(MediaType.APPLICATION_JSON)), userHandler::getUserById)
                ;


    }

    // @Bean
    // public RouterFunction<ServerResponse> user2(UserHandler userHandler) {
    //     return RouterFunctions.route()
    //             .GET("/flux2/users/list",userHandler::getAllUser)
    //
    //             .POST("/hello2",serverRequest -> {
    //                 String name=serverRequest.exchange().getRequest().getQueryParams().getFirst("name");
    //                 System.out.println("name："+name);
    //
    //                 assert name != null;
    //                 return ServerResponse.ok().bodyValue(name);
    //             })
    //             .build();
    // }
}
