package com.github.axinger.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Integer id;
    private String name;
    private Integer age;
    private LocalDateTime dateTime = LocalDateTime.now();
}
