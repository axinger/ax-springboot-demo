package com.github.axinger.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LoginDTO {

    @NotEmpty(message = "username不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在3-20个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字和下划线")
    private String username;

    @NotEmpty(message = "password不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    private String password;
}