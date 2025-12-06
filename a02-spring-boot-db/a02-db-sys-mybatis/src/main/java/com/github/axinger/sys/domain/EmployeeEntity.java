package com.github.axinger.sys.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.axinger.sys.model.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @TableName employee
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_employee")
public class EmployeeEntity implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
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
//    @TableField(value = "gender")
//    // 默认值,数据库为null, mp不会解析,代码可以默认值
//    private Gender gender;

    @TableField(value = "gender")
    @Builder.Default()
    private Gender gender = Gender.unknown;

    /**
     *
     */
    @TableField(value = "dept_id")
    private Integer deptId;

    @TableField(exist = false)
    private DepartmentEntity departmentEntity;
}
