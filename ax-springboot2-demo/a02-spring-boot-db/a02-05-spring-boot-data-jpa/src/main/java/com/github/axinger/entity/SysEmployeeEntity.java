package com.github.axinger.entity;

import com.github.axinger.dto.Gender;
import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

/**
 * 员工实体类（对应数据库表：sys_employee）
 * <p>
 * 表示组织中的员工信息。
 * 多个员工属于一个部门，是多对一关系中的“多”方。
 * </p>
 */
@Getter
@Setter
@ToString(exclude = "department") // 避免与部门互相引用导致 toString 循环
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "sys_employee",
        uniqueConstraints = @UniqueConstraint(columnNames = "email") // 唯一约束
)
public class SysEmployeeEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键 ID，自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 员工编号（工号）
     */
    private String code;

    /**
     * 员工姓名
     */
    private String name;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 性别，默认值为 UNKNOWN
     * 使用 Builder.Default() 确保构建时有默认值
     */
    @Builder.Default
    private Gender gender = Gender.UNKNOWN;

    /**
     * 所属部门（多对一关系）
     * <p>
     * 这是关系的“拥有方”（owning side），负责维护外键。
     * 外键字段位于当前表（sys_employee）中。
     * </p>
     * <p>
     * 注解说明：
     * - @ManyToOne：多对一关系（多个员工属于一个部门）
     * - fetch = FetchType.LAZY：懒加载，避免查询员工时立即加载部门完整信息
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "dept_id",                    // 当前表中外键字段名
            referencedColumnName = "id",         // 关联到 sys_department 表的 id 字段
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT) // 启用外键约束（推荐）
            // 生成 SQL 时会添加 FOREIGN KEY 约束，保证数据完整性
    )
    @Comment("所属部门ID")
    private SysDepartmentEntity department;


    // 在 SysEmployeeEntity 中添加（或工具类中）
    public SysEmployeeEntity withDepartment(SysDepartmentEntity dept) {
        this.setDepartment(dept);
        if (dept != null && !dept.getEmployees().contains(this)) {
            dept.getEmployees().add(this);
        }
        return this;
    }
}
