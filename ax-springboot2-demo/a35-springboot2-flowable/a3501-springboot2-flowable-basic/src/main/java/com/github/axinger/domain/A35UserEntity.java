package com.github.axinger.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @TableName employee
 */
@TableName(value = "a35_user_info")
@Data
public class A35UserEntity implements Serializable {
    @Serial
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

    @TableField(value = "password")
    private String password;
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
