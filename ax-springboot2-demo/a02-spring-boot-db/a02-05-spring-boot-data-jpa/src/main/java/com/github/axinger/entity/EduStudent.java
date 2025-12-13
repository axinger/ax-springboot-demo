package com.github.axinger.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

// 学生实体（拥有方）
@Entity(name = "edu_student")
@Getter
@Setter
@ToString(exclude = {"courses"})
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EduStudent {

    @Id
    private Long id;

    private String name;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(
            name = "edu_student_course",                    // 中间表名
            joinColumns = @JoinColumn(                 // 当前实体（Student）的外键
                    name = "student_id",                   // 中间表中的字段名
                    referencedColumnName = "id"           // Student 表中的主键
            ),
            inverseJoinColumns = @JoinColumn(         // 另一端实体（Course）的外键
                    name = "course_id",                    // 中间表中的字段名
                    referencedColumnName = "id"           // Course 表中的主键
            )
    )
    private List<EduCourse> courses = new ArrayList<>();
}
