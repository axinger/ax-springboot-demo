package com.github.axinger.db.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @TableName employee
 */
@TableName(value = "employee")
@Data
public class Employee implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     *
     */
    @TableField(value = "name")
    private String name;
    /**
     *
     */
    @TableField(value = "email")
    private String email;
    /**
     *
     */
    @TableField(value = "gender")
    private Integer gender;
    /**
     *
     */
    @TableField(value = "dept_id")
    private Integer deptId;


    @TableField(exist = false)
    private Department department;
}
