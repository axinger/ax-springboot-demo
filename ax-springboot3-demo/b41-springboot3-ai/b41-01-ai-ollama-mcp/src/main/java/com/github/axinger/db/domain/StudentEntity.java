package com.github.axinger.db.domain;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 学生表
 *
 * @TableName student
 */
@Data
public class StudentEntity implements Serializable {
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

    @Serial
    private static final long serialVersionUID = 1L;
}
