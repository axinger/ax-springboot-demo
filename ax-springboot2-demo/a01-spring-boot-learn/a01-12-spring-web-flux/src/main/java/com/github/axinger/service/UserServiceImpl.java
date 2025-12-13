package com.github.axinger.service;

import com.github.axinger.entity.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public Mono<User> getUserById(Integer id) {
        User user = User.builder()
                .id(id)
                .name("name" + id)
                .build();
        return Mono.justOrEmpty(user);
    }

    @Override
    public Flux<Object> getAllUser() {
        User user1 = User.builder()
                .id(1)
                .name("name" + 1)
                .build();

        User user2 = User.builder()
                .id(2)
                .name("name" + 2)
                .build();
        return Flux.fromIterable(List.of(user1, user2));

        // List<CompletableFuture<User>> list = List.of(getUserBy(1), getUserBy(2));
        // return Flux.fromStream(list.stream()).map(val -> val.join());
    }

    public CompletableFuture<User> getUserBy(int time) {

        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(time);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            User user = User.builder()
                    .id(time)
                    .name("name" + time)
                    .build();
            return user;
        });
    }


    @Override
    public Mono<Void> addUser(Mono<User> userMono) {

        return userMono.doOnNext(user -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).thenEmpty(Mono.empty());

    }
}
