package com.github.axinger.api.user.feign;

import com.github.axinger.api.user.dto.UserDTO;
import com.github.axinger.api.user.dto.UserRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户服务 Feign 接口定义
 * 服务提供方实现此接口，消费方通过 Feign 调用
 */
@RequestMapping("/api/user")
public interface UserFeignApi {

    @GetMapping("/{id}")
    UserDTO getById(@PathVariable("id") Long id);

    @GetMapping("/list")
    List<UserDTO> list();

    @PostMapping("/search")
    List<UserDTO> search(@RequestBody UserRequest request);

    @PostMapping
    UserDTO save(@RequestBody UserDTO user);
}
