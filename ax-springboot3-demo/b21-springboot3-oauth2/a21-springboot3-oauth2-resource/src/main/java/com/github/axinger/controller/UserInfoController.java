package com.github.axinger.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户信息控制器
 * 提供当前登录用户的信息查询
 */
@RestController
@RequestMapping("/api/user")
public class UserInfoController {

    /**
     * 获取当前用户基本信息
     * 需要 profile 权限
     */
    @GetMapping("/info")
    @PreAuthorize("hasAuthority('SCOPE_profile')")
    public Map<String, Object> userInfo(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("sub", jwt.getSubject());
        userInfo.put("username", jwt.getClaimAsString("sub"));
        userInfo.put("issuer", jwt.getIssuer().toString());
        userInfo.put("expiresAt", jwt.getExpiresAt());
        userInfo.put("scopes", jwt.getClaimAsStringList("scope"));
        return userInfo;
    }

    /**
     * 获取JWT令牌中的全部声明信息
     */
    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('SCOPE_profile')")
    public Map<String, Object> profile(@AuthenticationPrincipal Jwt jwt) {
        return jwt.getClaims();
    }
}
