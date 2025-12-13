package com.github.axinger.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "sys_user")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    @Column(name = "sex", length = 1)
    private Integer sex;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;
}
