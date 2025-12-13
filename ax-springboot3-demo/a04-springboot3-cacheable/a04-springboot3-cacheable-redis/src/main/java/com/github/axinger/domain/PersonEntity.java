package com.github.axinger.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @TableName person
 */
@TableName(value = "person")
@Data
public class PersonEntity implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 生日
     */
    private LocalDateTime birthday;
    /**
     * 性别
     */
    private Integer gender;
    /**
     * 身高
     */
    private Double height;
    /**
     * 姓名
     */
    private String name;
    /**
     * 体重
     */
    private Double weight;
    /**
     * 多租户id
     */
    private String tenantId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 更新次数
     */
    private Long updateCount;
    /**
     * 版本号
     */
    private Long version;
    /**
     * 逻辑删除
     */
    private Integer deleted;
}
