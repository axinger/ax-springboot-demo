package com.github.axinger.sys.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.github.axinger.sys.model.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_person")
public class SysPersonEntity implements Serializable {

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField(value = "name")
    private String name;

    @TableField(value = "age")
    private Integer age;

    @TableField(value = "gender")
    @Builder.Default()
    private Gender gender = Gender.unknown;

    @TableField(value = "birthday")
    private LocalDateTime birthday;


//    @TableField(value = "tenant_id")
//    private String tenantId;


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    /**
     * updateStrategy
     * 每种策略的作用：
     * 值	描述
     * IGNORED	忽略空值判断，实体对象的字段是什么值就用什么值更新，支持null值更新操作
     * NOT_NULL	进行非NULL判断，也是默认策略，相当于age!=null
     * NOT_EMPTY	进行非空判断，主要是针对字符串类型，相当于name != null and name != ‘’
     * NEVER	从不更新，不管字段是否有值，都不进行更新
     * DEFAULT	追随全局配置
     */
//    @Comment("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime; // 这个用date

    @Version
    @TableField(fill = FieldFill.INSERT)
    @Builder.Default()
    private Long version = 1L;

    @TableLogic
    @TableField(value = "deleted")
//    @Builder.Default()
    private Integer deleted;

}
