package com.github.axinger.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName employee
 */
@TableName(value = "ax_employee")
@Data
public class EmployeeEntity implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(value = "id")
    private String id;
    /**
     *
     */
    @TableField(value = "name")
    private String name;
    /**
     *
     */
    @TableField(value = "position")
    private String position;
    /**
     *
     */
    @TableField(value = "department_id")
    private String departmentId;
    /**
     *
     */
    @TableField(value = "direct_leader_id")
    private String directLeaderId;
}
