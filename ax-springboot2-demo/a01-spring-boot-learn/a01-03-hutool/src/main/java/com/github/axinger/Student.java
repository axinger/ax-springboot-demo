package com.github.axinger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    // 班级id
    private long classId;
    // 班级id
    private long studentId;
    // 学生名称
    private String name;
}
