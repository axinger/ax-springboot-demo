package com.github.axinger.controller;

import com.axing.common.response.dto.Result;
import com.github.axinger.domain.SysUserEntity;
import com.github.axinger.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private SysUserService userService;

    @GetMapping("/read")
//    @PreAuthorize("hasAuthority('user:read')")
    public Result<SysUserEntity> getUserInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        SysUserEntity user = userService.lambdaQuery()
                .eq(SysUserEntity::getUsername, username)
                .one();
        return Result.success(user);
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('user:list')")
    public Result<List<SysUserEntity>> listUsers() {
        List<SysUserEntity> users = userService.list();
        return Result.success(users);
    }

    //
//    @PostMapping("/create")
//    @PreAuthorize("hasAuthority('user:create')")
//    public Result<Void> createUser(@RequestBody @Valid UserDTO userDTO) {
//        userService.createUser(userDTO);
//        return Result.success();
//    }
//
    @PutMapping("/update")
//    @PreAuthorize("hasAuthority('user:update')")
    public Result<?> updateUser() {
//        userService.updateUser(userDTO);
        return Result.success("更新成功");
    }

    //
    @DeleteMapping("/delete/{id}")
//    @PreAuthorize("hasAuthority('user:delete')")
    public Result<?> deleteUser(@PathVariable Integer id) {
        return Result.success("删除成功");
    }
}
