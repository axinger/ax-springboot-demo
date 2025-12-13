package com.github.axinger.domain;

import com.mybatisflex.annotation.*;
import lombok.Data;

import java.util.List;

/**
 * 实体类。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@Data
@Table(value = "department")
public class Department {

    @Id(keyType = KeyType.Auto)
    private Long id;

    @Column(value = "name")
    private String name;

    @RelationOneToMany(selfField = "id", targetField = "deptId")
    private List<Employee> employeeList;

}
