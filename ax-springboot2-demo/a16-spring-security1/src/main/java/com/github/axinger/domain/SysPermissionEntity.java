package com.github.axinger.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 权限表
 *
 * @TableName sys_permission
 */
@TableName(value = "sys_permission")
@Data
public class SysPermissionEntity implements Serializable {
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
     * 权限编码
     */
    @TableField(value = "code")
    private String code;
    /**
     *
     */
    @TableField(value = "description")
    private String description;
    /**
     * 1-菜单 2-按钮 3-API
     */
    @TableField(value = "type")
    private Integer type;
    /**
     * 父权限ID
     */
    @TableField(value = "parent_id")
    private Integer parentId;
    /**
     * 访问路径
     */
    @TableField(value = "path")
    private String path;
    /**
     * 图标
     */
    @TableField(value = "icon")
    private String icon;
    /**
     * 排序
     */
    @TableField(value = "sort")
    private Integer sort;
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
}
