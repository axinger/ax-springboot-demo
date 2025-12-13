package com.github.axinger.validation;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Service
@Validated
public class NotEmptyService {

    public void strNotEmpty(@NotEmpty(message = "字符串不能空,可以空格") String name) {

        System.out.println("name = " + name);
    }
}
