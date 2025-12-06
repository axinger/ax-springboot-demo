package com.github.axinger.auth.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色表
 *
 * @TableName sys_roles
 */
@TableName(value = "sys_roles")
@Data
public class SysRolesEntity implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 角色ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 角色名称
     */
    @TableField(value = "name")
    private String name;
    /**
     * 角色编码
     */
    @TableField(value = "code")
    private String code;
    /**
     * 角色描述
     */
    @TableField(value = "description")
    private String description;
    /**
     * 排序
     */
    @TableField(value = "sort_order")
    private Integer sortOrder;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;
    /**
     * 版本号，用于乐观锁
     */
    @TableField(value = "version")
    private Integer version;
    /**
     * 逻辑删除标记，0-未删除，1-已删除
     */
    @TableField(value = "deleted")
    private Integer deleted;
}
