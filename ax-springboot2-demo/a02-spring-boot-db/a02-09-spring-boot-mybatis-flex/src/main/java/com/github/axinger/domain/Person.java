package com.github.axinger.domain;

import com.github.axinger.model.Gender;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 实体类。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@Data
@Table(value = "person")
public class Person {

    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 年龄
     */
    @Column(value = "age")
    private Integer age;

    /**
     * 生日
     */
    @Column(value = "birthday")
    private LocalDate birthday;

    /**
     * 性别
     */
    @Column(value = "gender")
    private Gender gender;

    /**
     * 身高
     */
    @Column(value = "height")
    private Double height;

    /**
     * 姓名
     */
    @Column(value = "name")
    private String name;

    /**
     * 体重
     */
    @Column(value = "weight")
    private Double weight;

    /**
     * 多租户id
     */
    @Column(value = "tenant_id", tenantId = true)
    private String tenantId;

    /**
     * 创建时间
     */
    @Column(value = "create_time", onInsertValue = "now()")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(value = "update_time", onUpdateValue = "now()")
    private LocalDateTime updateTime;

    /**
     * 更新次数
     */
    @Column(value = "update_count", onUpdateValue = "update_count + 1")
    private Long updateCount;

    /**
     * 版本号
     */
    @Column(value = "version", version = true)
    private Long version = 0L;

    /**
     * 逻辑删除
     */
    @Column(value = "deleted", isLogicDelete = true, jdbcType = JdbcType.INTEGER)
    private Integer deleted;


}
