package com.github.axinger.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 学生实体 — 演示 Redisson 存储自定义对象 (须实现 Serializable)
 */
@Data
public class Student implements Serializable, Comparable<Student> {

    private Long id;
    private String name;
    private Integer age;
    private LocalDateTime dateTime;

    @Override
    public int compareTo(Student obj) {
        return this.getId().compareTo(obj.getId());
    }
}
