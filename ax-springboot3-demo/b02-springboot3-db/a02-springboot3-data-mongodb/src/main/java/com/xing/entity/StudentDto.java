package com.xing.entity;

import lombok.Data;

import java.util.List;

@Data
public class StudentDto {
    private Integer id;
    private String className;
    private List<Student> studentList;
}
