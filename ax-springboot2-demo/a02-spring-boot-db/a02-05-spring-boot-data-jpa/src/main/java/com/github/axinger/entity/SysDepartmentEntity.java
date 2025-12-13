package com.github.axinger.entity;

import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 部门实体类（对应数据库表：sys_department）
 * <p>
 * 表示组织架构中的“部门”信息，如研发部、人事部等。
 * 一个部门可以包含多个员工，是一对多关系中的“一”方。
 * </p>
 */
@Entity(name = "sys_department") // JPA 实体名称（HQL 中使用），默认映射到表名 sys_department
@Table(name = "sys_department") // 指定数据库表名（推荐显式声明）
@Getter
@Setter
@ToString(exclude = {"employees"}) // 避免 toString 时无限递归（员工也引用部门）
@Builder // Lombok：生成流式创建对象的构造器
@AllArgsConstructor // Lombok：全参构造函数
@NoArgsConstructor  // Lombok：无参构造函数（JPA 要求）
public class SysDepartmentEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键 ID，自增
     * GenerationType.IDENTITY：使用数据库自增主键（如 MySQL AUTO_INCREMENT）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 部门编号（唯一标识）
     * length = 20：限制字段最大长度为 20 个字符
     * 数据库中对应字段：code VARCHAR(20)
     */
    @Column(name = "code", length = 20)
    @Comment("部门编号") // Hibernate 注解：为字段添加数据库注释（支持 MySQL/PostgreSQL）
    private String code;

    /**
     * 部门名称
     * 如：“技术研发部”、“人力资源部”
     * length = 20：限制长度
     */
    @Column(name = "name", length = 20)
    @Comment("部门名称")
    private String name;

    /**
     * 该部门下的员工列表（一对多关系）
     * <p>
     * 关键点：
     * - mappedBy = "department"：表示关系由 SysEmployeeEntity 中的 department 字段维护
     * - 即：外键在 employee 表中（dept_id），本类不直接管理外键
     * - 因此当前类是“被拥有方”（inverse side）
     * </p>
     * <p>
     * 属性说明：
     * - cascade = CascadeType.ALL：级联操作（保存/更新/删除部门时，自动处理员工）
     * - fetch = FetchType.LAZY：懒加载，避免查询部门时立即加载所有员工（性能优化）
     * - orphanRemoval = false：不启用“孤儿删除”
     * - 若设为 true，则当 employee.department = null 时，会自动删除该员工记录
     */
    @OneToMany(
            mappedBy = "department",           // 对应 SysEmployeeEntity.department 字段
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = false
    )
    private List<SysEmployeeEntity> employees;


    // 安全添加方法
    public void addEmployee(SysEmployeeEntity employee) {
        if (this.employees == null) {
            this.employees = new ArrayList<>();
        }
        this.employees.add(employee);
        /// 互相添加对方
        employee.setDepartment(this); // 维护 owning side
    }

    public void removeEmployee(SysEmployeeEntity employee) {
        if (this.employees == null) {
            this.employees = new ArrayList<>();
        }
        this.employees.remove(employee);
        employee.setDepartment(null);
    }
}
