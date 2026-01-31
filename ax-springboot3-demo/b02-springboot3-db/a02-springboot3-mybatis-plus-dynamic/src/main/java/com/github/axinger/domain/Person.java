package com.github.axinger.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @TableName t_person
 */
@TableName(value = "t_person")
@Data
public class Person implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 年龄
     */
    @TableField(value = "age")
    private Integer age;
    /**
     * 生日
     */
    @TableField(value = "birthday")
    private Date birthday;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 逻辑删除
     */
    @TableField(value = "deleted")
    private Integer deleted;
    /**
     * 性别
     */
    @TableField(value = "gender")
    private Integer gender;
    /**
     * 身高
     */
    @TableField(value = "height")
    private Double height;
    /**
     * 姓名
     */
    @TableField(value = "name")
    private String name;
    /**
     * 多租户id
     */
    @TableField(value = "tenant_id")
    private String tenantId;
    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    /**
     * 版本号
     */
    @TableField(value = "version")
    private Long version;
    /**
     * 体重
     */
    @TableField(value = "weight")
    private Double weight;
}
