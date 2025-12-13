package com.github.axinger.validation;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Service
@Validated
public class ValidationController {

    /// 能校验 " "
    public String test(@NotBlank(message = "name不能为空字符串") String name) {

        return name;
    }

    /// 不能校验 " "
    public String test2(@NotEmpty(message = "name不能为空字符串") String name) {

        return name;
    }
}
