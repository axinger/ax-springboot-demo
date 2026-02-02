package com.github.axinger.controller;

import com.github.axinger.model.dto.LoginDTO;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/get")
@Validated // get请求校验参数,不封装
public class GetController {

    @GetMapping("/login")
    public Object login(@RequestParam("username") @NotEmpty(message = "username不能为空") String username,
                        @RequestParam @NotBlank(message = "password不能为空") @Length(min = 2, message = "password长度不能小于2") String password) {
        return List.of(username, password);
    }

    /// get请求参数封装对象
    @GetMapping("/login2")
    public Object login2(@Validated LoginDTO dto) {
        return dto;
    }

    @GetMapping("/login3")
    public Object login3(@Validated @ModelAttribute LoginDTO dto) {
        return dto;
    }

    @GetMapping({
            "/test1",
            "/test1/{id}"
    })
    @Validated
    public Object test1(@PathVariable(required = false, name = "id") String id) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("id", id);
        return map;
    }

    /// get参数list  [...](http://localhost:12021/get/test2?param=2&param=1)
    /// [...](http://localhost:12021/get/test2?param=1,2,3)",
    @GetMapping("/test2")
    public Object test3(@RequestParam("param") List<String> id) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("id", id);
        return map;
    }


    @GetMapping("/test3")
    public Object test3(
            @RequestParam(required = false, defaultValue = "false") Boolean hit,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) LocalDate localDate
    ) {
        Map<String, Object> result = new HashMap<>();
        result.put("hit", hit);
        result.put("age", age);
        result.put("name", name);
        result.put("localDate", localDate);
        return result;
    }

    @Data
    public static class Test4DTO {
        Boolean hit;
        Integer age;
        String name;
        private LocalDate localDate;
    }

    @GetMapping("/test4")
    public Object test3(Test4DTO dto) {
        return dto;
    }

    /**
     * 示例：带校验的路径变量
     */
    @GetMapping("/test9/{id}")
    public Object test9(@PathVariable @NotBlank(message = "用户ID不能为空") String id) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        return result;
    }

    @GetMapping("/test10")
    public Object test10(LocalDateTime dateTime) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", dateTime);
        return result;
    }

    /*
       #{T(...)} 是 SpEL（Spring Expression Language，
        Spring
         表达式语言） 的一种语法，用于在表达式中引用 Java 类（Class），从而可以调用该类的 静态方法 或访问其 静态字段。

      @Value("#{T(java.lang.Math).sqrt(16)}")
      @ConditionalOnExpression("#{T(org.apache.commons.lang3.StringUtils).isNotBlank('${my.property}')}")
     */
    @Value("#{T(com.github.axinger.config.MyValueFactory).path('/test11')}")
    private String path;

    @Value("#{@myValueFactory2.path('/test11')}")
    private String path2;

    @Value("#{T(java.lang.Integer).parseInt('${app.timeout:2}') * 1000}")
    private int timeoutInMillis;

    @GetMapping(value = {
            "/test11",
            "${path.test11:/test13}",
            "#{T(com.github.axinger.config.MyValueFactory).path('/test11')}",
            "#{@myValueFactory2.path('/test11')}"
    })
    public Object test11() {
        Map<String, Object> result = new HashMap<>();
        result.put("path", path);
        result.put("path2", path2);
        result.put("timeoutInMillis", timeoutInMillis);
        return result;
    }

}
