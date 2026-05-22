package com.github.axinger.controller;

import com.github.axinger.entity.SysUser;
import com.github.axinger.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户信息控制器
 */
@RestController
@RequiredArgsConstructor
public class UserController {

    private final SysUserService sysUserService;

    /**
     * OIDC 用户信息端点
     */
    @GetMapping("/userinfo")
    public ResponseEntity<Map<String, Object>> userInfo(Principal principal) {
        SysUser user = sysUserService.findByUsername(principal.getName());
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("sub", user.getId().toString());
        userInfo.put("name", user.getUsername());
        userInfo.put("email", user.getEmail());
        userInfo.put("phone", user.getPhone());
        userInfo.put("real_name", user.getRealName());
        if (user.getRoles() != null) {
            userInfo.put("roles", user.getRoles().stream().map(r -> r.getRoleCode()).toList());
        }
        return ResponseEntity.ok(userInfo);
    }
}
