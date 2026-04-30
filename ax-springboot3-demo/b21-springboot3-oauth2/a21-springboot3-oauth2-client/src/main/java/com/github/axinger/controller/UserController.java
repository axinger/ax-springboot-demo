package com.github.axinger.controller;

import com.github.axinger.service.ResourceServerClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

/**
 * 用户信息控制器
 */
@Controller
@RequiredArgsConstructor
public class UserController {

    private final ResourceServerClient resourceServerClient;

    /**
     * 用户信息页面
     */
    @GetMapping("/user")
    public String user(Model model, @AuthenticationPrincipal OAuth2User oauth2User, OAuth2AuthenticationToken authentication) {
        if (oauth2User != null) {
            model.addAttribute("oauth2User", oauth2User.getAttributes());
            model.addAttribute("clientName", authentication.getAuthorizedClientRegistrationId());
        }

        // 从资源服务器获取用户信息
        try {
            Map<String, Object> userInfo = resourceServerClient.getUserInfo();
            model.addAttribute("resourceUserInfo", userInfo);
        } catch (Exception e) {
            model.addAttribute("resourceError", "无法从资源服务器获取用户信息: " + e.getMessage());
        }

        return "user";
    }
}
