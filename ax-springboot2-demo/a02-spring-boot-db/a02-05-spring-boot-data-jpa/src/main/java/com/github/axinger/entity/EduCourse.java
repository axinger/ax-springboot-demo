package com.github.axinger.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;


// 课程实体（被拥有方，使用 mappedBy）
@Entity(name = "edu_course")
@Getter
@Setter
@ToString(exclude = {"students"})
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EduCourse {

    @Id
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    private List<EduStudent> students = new ArrayList<>();
}
