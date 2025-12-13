package com.github.axinger.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity(name = "sys_address")
// @Proxy(lazy = false)
@Getter
@Setter

// @Getter
// @Setter
// @AllArgsConstructor
// @NoArgsConstructor
@EqualsAndHashCode(exclude = "user")// jpa中使用lombok时，需排除关联表属性，否则会报错
@ToString(exclude = "user")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String detail;

    /**
     * 双向关联
     */
    // @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL, optional = false)////可选属性optional=false,表示user不能为空。删除地址，不影响用户
    // @ManyToOne
    // @JoinColumn(name = "user_id")
    // @JsonBackReference // 防止json序列化出现死循环
    // private Users user;

    // @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    // @JoinTable(name = "user_id")
    // private Users user;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinTable(name = "user_to_address")
    private Person user;

}
