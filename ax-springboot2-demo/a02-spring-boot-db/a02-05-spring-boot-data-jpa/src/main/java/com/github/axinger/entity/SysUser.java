package com.github.axinger.entity;

import com.github.axinger.dto.Gender;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "sys_user")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "userAddress")
public class SysUser {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO) //pgsql使用 自增,有nextval('book_id_seq'::regclass),连续
    @GeneratedValue(strategy = GenerationType.IDENTITY) //IDENTITY：使用数据库的自增机制（如 MySQL 的 AUTO_INCREMENT）
    @Column(name = "id")
    private Long id;

    @Column(name = "username", length = 20)
    private String username;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "age")
    private int age;

    //@com.alibaba.fastjson2.annotation.JSONField(
//        serializeUsing = GenderObjectWriter.class,  // ✅ 用 serializeUsing
//        deserializeUsing = GenderObjectReader.class // ✅ 用 deserializeUsing
//)
    @Column(name = "sex", length = 1)
//    @Convert(converter = GenderConverter.class) //手动添加
//    @Enumerated(EnumType.STRING)
//    枚举值以 name() 形式存储，如 "MALE", "FEMALE"
//    安全、可读性强，推荐用于大多数场景。
//    ✅ 数据库值：'MALE', 'FEMALE'
    private Gender sex;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;


//    @OneToMany(mappedBy = "sysUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<SysUserAddress> userAddress;

    // 软外键, 懒加载 配合  @EntityGraph(attributePaths = "userAddress") // 指定要加载的关联字段
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "sys_user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    private List<SysUserAddress> userAddress;

//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER) // 改为 EAGER
//    @JoinColumn(
//            name = "sys_user_id",
//            referencedColumnName = "id",
//            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
//    )
//    private List<SysUserAddress> userAddress;

    /// 添加一个监听方法
//    @PrePersist
//    @PreUpdate
//    private void updateAddressUserIds() {
//        if (userAddress != null && getId() != null) {
//            for (SysUserAddress address : userAddress) {
//                address.setSysUserId(getId()); // 自动填充 sysUserId
//            }
//        }
//    }
}
