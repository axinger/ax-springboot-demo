package com.github.axinger.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "sys_student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @Id
    // @GeneratedValue(strategy = GenerationType.AUTO, generator = "myid")
    // @GenericGenerator(name = "myid", strategy = "com.git.hui.boot.jpa.generator.ManulInsertGenerator")
    // @Column(name = "id")
    private Integer id = 0;

    @Column
    private String name;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "t_student_to_subject", joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    /**
     * 实际生活中，选修这个动作是由学生来完成，所以这里，学生和课程的关联关系维护方，也指定给学生。
     */
    private List<Subject> subjectList = new ArrayList<>();

}
