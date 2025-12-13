package com.github.axinger.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 角色表
 *
 * @TableName sys_role
 */
@TableName(value = "sys_role")
@Data
public class SysRoleEntity implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     *
     */
    @TableField(value = "name")
    private String name;
    /**
     * 角色编码
     */
    @TableField(value = "code")
    private String code;
    /**
     *
     */
    @TableField(value = "description")
    private String description;
    /**
     * 0-禁用 1-启用
     */
    @TableField(value = "status")
    private Integer status;
    /**
     *
     */
    @TableField(value = "create_time")
    private Date createTime;
    /**
     *
     */
    @TableField(value = "update_time")
    private Date updateTime;
    /**
     *
     */
    @TableField(value = "version")
    private Long version;
    /**
     *
     */
    @TableField(value = "is_deleted")
    private Integer isDeleted;
    @TableField(exist = false)
    private List<SysPermissionEntity> permissions; // 角色拥有的权限集合
}
