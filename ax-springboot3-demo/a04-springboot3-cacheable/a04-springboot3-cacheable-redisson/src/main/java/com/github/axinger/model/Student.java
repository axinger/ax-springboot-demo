package com.github.axinger.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

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
