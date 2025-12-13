package com.github.axinger.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;


@Entity(name = "teach_school_class")
@Getter
@Setter
@ToString(exclude = "school")
//@EqualsAndHashCode(exclude = "school")
public class SchoolClass {
    /**
     * 班级id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 班级名称
     */
    @Column
    private String roomName;

    /**
     * 楼层
     */
    @Column
    private String layer;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    // @JoinTable(name = "t_school_to_room") // 用一个中间表

    @JoinTable(name = "teach_school_class_to_school", joinColumns = @JoinColumn(name = "school_class_id"),
            inverseJoinColumns = @JoinColumn(name = "school_id"))
    private School school;

    /**
     * 一所班级有多个学生是“一对多”的关系，
     * 注意：这里的学生需要使用集合类型
     */
    // @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    // private List<Student> studentList;

    // @Override
    // public String toString() {
    //
    //     return "Room{" +
    //             "id=" + id +
    //             ", roomName='" + roomName + '\'' +
    //             // ", studentList='" + studentList + '\'' +
    //             '}';
    // }
}
