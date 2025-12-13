package com.github.axinger.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.annotation.Version;
import com.github.axinger.model.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@TableName(value = "person")
@Entity(name = "person")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonEntity implements Serializable {

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY) //pgsql使用 自增,有nextval('book_id_seq'::regclass),连续
//    @GeneratedValue(strategy = GenerationType.AUTO) // mysql使用  自增但不连续,mysql会维护一个主键表
    @GeneratedValue(strategy = GenerationType.IDENTITY) // mysql使用
    @Column()
    private Long id;

    @TableField(value = "name")
    @Column
    @Comment("姓名")
    private String name;

    @TableField(value = "age")
    @Column
    @Comment("年龄")
    @OrderColumn(name = "age")
    private Integer age;

    @Column
    @TableField(value = "gender")
    // 默认值,数据库为null, mp不会解析,代码可以默认值
    @Comment("性别")
    @OrderColumn()
    private Gender gender = Gender.unknown;

    /**
     *
     */
    // @TableField(value = "book_price",updateStrategy = FieldStrategy.NEVER) // 自动+1,再set值,就无效,@Version原理如此
    @TableField(value = "height") // 自动+1,再set值,就无效,@Version原理如此
    @Column
    @Comment("身高")
    private Double height;

    @TableField(value = "weight")
    @Column
    @Comment("体重")
    private Double weight;

    @TableField(value = "birthday")
    @Column
    @Comment("生日")
    private LocalDateTime birthday;


    @TableField(value = "tenant_id")
    @Column
    @Comment("多租户id")
    private String tenantId;


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @Column
    @Comment("创建时间")
    private LocalDateTime createTime;


    /**
     * 每种策略的作用：
     * 值	描述
     * IGNORED	忽略空值判断，实体对象的字段是什么值就用什么值更新，支持null值更新操作
     * NOT_NULL	进行非NULL判断，也是默认策略，相当于age!=null
     * NOT_EMPTY	进行非空判断，主要是针对字符串类型，相当于name != null and name != ‘’
     * NEVER	从不更新，不管字段是否有值，都不进行更新
     * DEFAULT	追随全局配置
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE, updateStrategy = FieldStrategy.IGNORED)
    // @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE,update="now()")
    // @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    // @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    @Comment("更新时间")
    private Date updateTime; // 这个用date

    @Version
    @TableField(fill = FieldFill.INSERT)
    @Comment("版本号")
    private Long version;

    @TableField(value = "deleted")
    @TableLogic
    @Column
    @Comment("逻辑删除")
    @Builder.Default
    private Integer deleted = 0;
}
