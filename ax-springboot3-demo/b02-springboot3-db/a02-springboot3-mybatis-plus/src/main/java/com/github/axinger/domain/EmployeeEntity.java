package com.github.axinger.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.axinger.model.Gender;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @TableName employee
 */
@Data
@TableName(value = "employee")
@Entity(name = "employee")
public class EmployeeEntity implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    /**
     *
     */
    @Column
    @TableField(value = "name")
    private String name;
    /**
     *
     */
    @Column
    @TableField(value = "email")
    private String email = "";

    /**
     *
     */
    @Column
    @TableField(value = "gender")
    // 默认值,数据库为null, mp不会解析,代码可以默认值
    private Gender gender = Gender.unknown;
    /**
     *
     */
    @Column
    @TableField(value = "dept_id")
    private Integer deptId;

    @TableField(exist = false)
    @Transient
    private DepartmentEntity departmentEntity;
}
