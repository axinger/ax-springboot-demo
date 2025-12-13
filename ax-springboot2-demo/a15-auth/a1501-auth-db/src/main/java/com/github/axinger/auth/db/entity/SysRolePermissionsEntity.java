package com.github.axinger.auth.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色权限关联表
 *
 * @TableName sys_role_permissions
 */
@TableName(value = "sys_role_permissions")
@Data
public class SysRolePermissionsEntity implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 角色ID
     */
    @TableField(value = "role_id")
    private Long roleId;
    /**
     * 权限ID
     */
    @TableField(value = "permission_id")
    private Long permissionId;
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
