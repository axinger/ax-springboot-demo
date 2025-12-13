package com.github.axinger.entity;

import lombok.*;

import javax.persistence.*;


@Entity(name = "sys_user_address")
// @Proxy(lazy = false)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
// @Getter
// @Setter
// @AllArgsConstructor
// @NoArgsConstructor
//@EqualsAndHashCode(exclude = "user")// jpa中使用lombok时，需排除关联表属性，否则会报错
//@ToString(exclude = "user")
public class SysUserAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;

    private String street;

    private String detail;

//    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
//    @JoinTable(name = "sys_user_to_address")
//    private SysUser sysUser;


    @Column(name = "sys_user_id", nullable = false)
    private Long sysUserId;


// 这种不会创建中间表
//    标准的 @OneToMany 应该是在 “多”的一方（即 SysUserAddress）中定义 @ManyToOne，并让 sys_user_id 作为外键字段存在该表中，而不是用 @JoinTable
//    @ManyToOne
//    @JoinColumn(name = "sys_user_id")
//    private SysUser sysUser;

// 会创建中间表
//    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
//    @JoinTable(name = "sys_user_to_address",
//            joinColumns = @JoinColumn(name = "sys_user_id"),
//            inverseJoinColumns = @JoinColumn(name = "sys_user_address_id"))
//    private List<SysUserAddress> userAddress;

    // 软外键
    // 可选：添加 transient 字段用于运行时关联（不映射到数据库）
    @Transient
    private SysUser sysUser;
}
