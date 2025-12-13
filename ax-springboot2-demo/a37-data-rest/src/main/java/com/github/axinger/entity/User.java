package com.github.axinger.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity(name = "base_user")
@Data
public class User implements Serializable {
//    @Version
//    Long version;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Comment("主键")
    private Long id;

    @Column(name = "name")
    @Comment("昵称")
    private String name;

    @Column(name = "age")
    @Comment("年龄")
    private Integer age;

    @Column(name = "email")
    private String email;

    @Column(name = "birthday")
    @Comment("生日")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime birthday;


}
