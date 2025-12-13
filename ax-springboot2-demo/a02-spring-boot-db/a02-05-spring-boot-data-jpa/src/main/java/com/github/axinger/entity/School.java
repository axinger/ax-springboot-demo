package com.github.axinger.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * (School)实体类
 *
 * @author Makavelli
 * @since 2022-07-13 10:56:27
 */
@Getter
@Setter
@Entity(name = "teach_school")
public class School {
    /**
     * 学校id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 学校名称
     */
    @Column
    private String schoolName;

    /**
     * 所属区域
     */
    @Column
    private String zone;

    /**
     * 占地面积
     */
    @Column
    private Double area;

    /**
     * 一所学校有多个班级是“一对多”的关系，
     * 注意：这里的班级需要使用集合类型
     * 级联保存、更新、删除、刷新;延迟加载。当删除用户，会级联删除该用户的所有文章
     * 拥有mappedBy注解的实体类为关系被维护端,mappedBy="school"中的 school 是 Room 中的 school 属性
     */
    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<SchoolClass> schoolClassList;

    /**
     * 一所学校通常只有一个校长,我们可以将其视作：“一对一”的关系
     * fetch = FetchType.EAGER : 急加载
     * cascade = CascadeType.PERSIST : 级联新增 ；当新增School时，如果SchoolMaster中的数据在数据库中不存在，会级联新增
     * OneToOne的注解添加到那个类中，就表示由此类维护关联关系。会在此类对应的表中新增级联字段。
     */
    // @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    // @JoinTable(name = "school_to_master",// 此处设置关联关系表名
    //         joinColumns = @JoinColumn(name = "school_id"),// 此处指定关联表中，对应关联关系维护端的字段名，若不指定jpa会自动生成
    //         inverseJoinColumns = @JoinColumn(name = "master_id"))//此处指定关联表中，对应关联关系被维护端的字段名，若不指定jpa会自动生成
    // private SchoolMaster schoolMaster;
}
