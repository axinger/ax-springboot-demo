package com.github.axinger.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
public class UserDTO {
    @NotEmpty(message = "name不能为空")
    private String name;

    @Max(value = 100)
    @Min(value = 18, message = "年龄不能小于18岁")
    private int age;

    @NotEmpty(message = "username不能为空", groups = ILoginGroup.class)
    private String username;


    @NotEmpty(message = "password不能为空", groups = ILoginGroup.class)
    private String password;
}
