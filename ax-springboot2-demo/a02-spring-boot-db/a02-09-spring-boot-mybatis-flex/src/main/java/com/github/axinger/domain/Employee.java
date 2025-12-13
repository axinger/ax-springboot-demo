package com.github.axinger.domain;

import com.github.axinger.model.Gender;
import com.mybatisflex.annotation.*;
import lombok.Data;

/**
 * 实体类。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@Data
@Table(value = "employee")
public class Employee {

    @Id(keyType = KeyType.Auto)
    private Long id;

    @Column(value = "dept_id")
    private Integer deptId;

    @Column(value = "email")
    private String email;

    @Column(value = "gender")
    private Gender gender;

    @Column(value = "name")
    private String name;


    @RelationManyToOne(selfField = "deptId", targetField = "id", selectColumns = {"id", "name"})
    private Department department;

}
