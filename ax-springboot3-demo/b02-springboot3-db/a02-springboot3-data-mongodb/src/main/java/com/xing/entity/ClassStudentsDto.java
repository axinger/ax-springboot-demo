package com.xing.entity;

import lombok.Data;

import java.util.List;

@Data
public class ClassStudentsDto {
    private Integer id;
    private String username;
    private Integer classId;
    private List<StudentClass> classStudents;
}
