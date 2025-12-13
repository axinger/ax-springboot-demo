package com.github.axinger.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName department
 */
@TableName(value = "ax_department")
@Data
public class DepartmentEntity implements Serializable {
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
    @TableField(value = "dept_leader_id")
    private String deptLeaderId;
    /**
     *
     */
    @TableField(value = "company_leader_id")
    private String companyLeaderId;
}
