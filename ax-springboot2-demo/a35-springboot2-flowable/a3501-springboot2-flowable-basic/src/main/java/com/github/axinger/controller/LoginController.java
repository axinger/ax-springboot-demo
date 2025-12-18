package com.github.axinger.controller;

import com.github.axinger.domain.A35UserEntity;
import com.github.axinger.service.OrgService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private OrgService orgService;

    @PostMapping("/api/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        try {
            // 简单验证，实际项目中应该使用更安全的验证方式

            A35UserEntity user = orgService.getUserByName(loginRequest.getUsername());

            // 这里我们假设所有用户都使用相同的密码进行测试
            // 在实际应用中，你应该使用加密密码进行比较
            if (user != null && user.getPassword().equals(loginRequest.getPassword())) {
                // 将用户信息存储在session中
                session.setAttribute("user", user);

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "登录成功");
                response.put("user", user);
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "用户名或密码错误");
                return ResponseEntity.status(401).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "登录失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/api/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpSession session) {
        session.invalidate();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "登出成功");
        return ResponseEntity.ok(response);
    }

    // 登录请求的数据传输对象
    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }
}
