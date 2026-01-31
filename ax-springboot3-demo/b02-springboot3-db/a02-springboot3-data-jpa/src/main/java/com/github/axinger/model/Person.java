package com.github.axinger.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// @Proxy(lazy = false)
@Entity(name = "sys_person")
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uc_person_user_name_age", columnNames = {"user_name", "age"})
})
@Getter
@Setter
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_name")
    private String userName;

    private int age;


    /**
     * 1. 一对一
     * 一对一映射需要@OneToOne注解和@JoinColumn注解配合使用
     * <p>
     * 注意：
     *
     * @JoinColumn 是指表与表之间关系的字段
     * @OneToOne是一对一关系映射。其中CascadeType是级联类型。 CascadeType.PERSIST
     * <p>
     * 级联新增，保存父对象时会新建其中包含的子对象
     * <p>
     * CascadeType.MERGE
     * <p>
     * 级联修改，保存父对象时会更新其中所包含的子对象数据
     * <p>
     * CascadeType.REMOVE
     * <p>
     * 级联删除，当删除关联关系时会将子对象的数据删除
     * <p>
     * CascadeType.REFRESH
     * <p>
     * 级联刷新，保存关联关系时会更新子对象和数据库中一致(意思是你在父对象中添加一个只包含ID的子对象，也可以保存进去)
     * <p>
     * CascadeType.ALL
     * <p>
     * 包含上述所有操作
     */
    //@JoinColumn注解中的name元素为当前实体类中对应的属性id，即users表中的card_id
    // 而referencedColumnName则为关联对象的id,即cards表中的主键id
//    @JoinColumn(name = "card_id", referencedColumnName = "id")
//    @OneToOne(cascade = {CascadeType.ALL})
//    private Cards card;

    /**
     * 注意:这里新增了一个注解
     *
     * @OneToMany(mappedBy = "user") 代表一对多的关系
     * <p>
     * 其中属性mappedBy的意思是:
     * <p>
     * 1.只有OneToOne，OneToMany，ManyToMany上才有mappedBy属性，ManyToOne不存在该属性,在@OneToMany里加入mappedBy属性可以避免生成一张中间表。
     * <p>
     * 2.mappedBy标签一定是定义在被拥有方的，他指向拥有方； 表示声明自己不是一对多的关系维护端，由对方来维护，是在一的一方进行声明的。mappedBy的值应该为一的一方的表名
     * <p>
     * 3.mappedBy的含义，应该理解为，拥有方能够自动维护跟被拥有方的关系，当然，如果从被拥有方，通过手工强行来维护拥有方的关系也是可以做到的；
     * <p>
     * 4.mappedBy跟joinColumn/JoinTable总是处于互斥的一方，可以理解为正是由于拥有方的关联被拥有方的字段存在，拥有方才拥有了被拥有方。mappedBy这方定义JoinColumn/JoinTable总是失效的，不会建立对应的字段或者表。
     */
    // @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    // @JsonBackReference // 防止json序列化出现死循环
    // private List<Address> addressList = new ArrayList<>();

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    private List<Address> addressList;
//
//    @ManyToMany
//    @JoinTable(name = "user_task", joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "task_id"))
//    private List<Tasks> tasksList;

}
