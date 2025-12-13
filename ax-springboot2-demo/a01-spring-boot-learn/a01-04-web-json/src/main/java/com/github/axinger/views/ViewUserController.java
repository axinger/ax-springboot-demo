package com.github.axinger.views;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.axinger.model.ApiViews;
import com.github.axinger.model.Views;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ViewUserController {

    @JsonView(Views.Public.class)
    @GetMapping("/public/profile")
    public User getPublicProfile() {
        User user = new User();
        user.setUsername("Alice");
        user.setEmail("alice@example.com");
        user.setPasswordHash("123456");
        return user;
    }

    @JsonView(Views.Internal.class)
    @GetMapping("/internal/profile")
    public User getInternalProfile() {
        User user = new User();
        user.setUsername("Alice");
        user.setEmail("alice@example.com");
        user.setPasswordHash("123456");
        return user;
    }

    @JsonView(ApiViews.V1.class)
    public List<User> getAllUsers() {
        User user = new User();
        user.setUsername("Alice");
        user.setEmail("alice@example.com");
        user.setPasswordHash("123456");
        return List.of(user);
    }
}
