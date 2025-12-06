package com.github.axinger.auth.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 权限API关联表
 *
 * @TableName sys_permission_apis
 */
@TableName(value = "sys_permission_apis")
@Data
public class SysPermissionApisEntity implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 权限ID
     */
    @TableField(value = "permission_id")
    private Long permissionId;
    /**
     * API ID
     */
    @TableField(value = "api_id")
    private Long apiId;
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
