package com.github.axinger.projecta.controller;

import com.github.axinger.api.user.dto.UserDTO;
import com.github.axinger.api.user.dto.UserRequest;
import com.github.axinger.api.user.feign.UserFeignApi;
import com.github.axinger.common.result.Result;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 用户服务 Controller
 * 实现了 UserFeignApi 接口，对外提供用户服务
 */
@RestController
public class UserController implements UserFeignApi {

    private final AtomicLong idGenerator = new AtomicLong(1);
    private final List<UserDTO> users = new ArrayList<>();

    public UserController() {
        // 初始化一些测试数据
        users.add(UserDTO.builder().id(idGenerator.getAndIncrement()).username("zhangsan").email("zhangsan@example.com").age(25).build());
        users.add(UserDTO.builder().id(idGenerator.getAndIncrement()).username("lisi").email("lisi@example.com").age(30).build());
    }

    @Override
    public UserDTO getById(Long id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<UserDTO> list() {
        return users;
    }

    @Override
    public List<UserDTO> search(UserRequest request) {
        return users.stream()
                .filter(u -> request.getUsername() == null || u.getUsername().contains(request.getUsername()))
                .filter(u -> request.getMinAge() == null || u.getAge() >= request.getMinAge())
                .filter(u -> request.getMaxAge() == null || u.getAge() <= request.getMaxAge())
                .toList();
    }

    @Override
    public UserDTO save(UserDTO user) {
        if (user.getId() == null) {
            user.setId(idGenerator.getAndIncrement());
            users.add(user);
        } else {
            users.removeIf(u -> u.getId().equals(user.getId()));
            users.add(user);
        }
        return user;
    }

    /**
     * 额外暴露一个非 Feign 接口的端点，用于测试
     */
    public Result<String> hello() {
        return Result.ok("Hello from Project-A (User Service)");
    }
}
