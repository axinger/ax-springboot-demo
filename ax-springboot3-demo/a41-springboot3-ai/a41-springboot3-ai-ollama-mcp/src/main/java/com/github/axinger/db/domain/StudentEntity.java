package com.github.axinger.db.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 学生表
 * @TableName student
 */
@TableName(value ="student")
@Data
public class StudentEntity implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id")
    private Integer id;

    /**
     * 姓名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 年龄
     */
    @TableField(value = "age")
    private Integer age;

    /**
     * 性别
     */
    @TableField(value = "sex")
    private String sex;

    /**
     * 班级
     */
    @TableField(value = "class_room")
    private String classRoom;

    /**
     * 家庭住址
     */
    @TableField(value = "address")
    private String address;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
