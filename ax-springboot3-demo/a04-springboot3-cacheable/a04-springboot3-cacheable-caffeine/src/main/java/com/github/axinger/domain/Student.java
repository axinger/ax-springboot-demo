package com.github.axinger.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName t_student
 */
@TableName(value = "t_student")
@Data
public class Student implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     *
     */
    @TableField(value = "name")
    private String name;
    /**
     *
     */
    @TableField(value = "age")
    private Integer age;
    /**
     *
     */
    @TableField(value = "gender")
    private Integer gender;
    /**
     *
     */
    @TableField(value = "address")
    private String address;
    /**
     *
     */
    @TableField(value = "create_time")
    @Schema(hidden = true)
    private Date createTime;
    /**
     *
     */
    @TableField(value = "update_time")
    @Schema(hidden = true)
    private Date updateTime;
    /**
     *
     */
    @TableField(value = "version")
    @Schema(hidden = true)
    private Long version;
    /**
     *
     */
    @TableField(value = "deleted")
    @Schema(hidden = true)
    private Integer deleted;
}
