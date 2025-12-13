package com.github.axinger.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "sys_subject")
// @ToString(exclude = "studentList")
// @EqualsAndHashCode(exclude = "studentList")
public class Subject {

    /**
     * TABLE：使用一个特定的数据库表格来保存主键。 
     * SEQUENCE：根据底层数据库的序列来生成主键，条件是数据库支持序列。 
     * IDENTITY：主键由数据库自动生成（主要是自动增长型） 主键则由数据库自动维护，使用起来很简单
     * AUTO：主键由程序控制。 
     * <p>
     * mysql数据库一般使用IDENTITY
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id = 0;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "subjectList")
    private List<Student> studentList = new ArrayList<>();
}
