package com.github.axinger.model;

import lombok.Data;

@Data
public class StudentPageDTO {

    private int current;
    private int size;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别
     */
    private String sex;

    /**
     * 班级
     */
    private String classRoom;

    /**
     * 家庭住址
     */
    private String address;
}
