package com.xing.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description：学生信息实体类
 * @Author :zks
 * @Date :9:25 2020/9/15
 */
@Data// lombok插件
@Document(collection = "studentInfo")// 可以省略，如果省略，则默认使用类名小写映射集合
public class StudentInfo {
    // 主键标识，该属性的值会自动对应mongodb的主键字段"_id"，如果该属性名就叫“id”,则该注解可以省略，否则必须写
    @Id
    private ObjectId id;
    /**
     * 学生姓名
     */
    // 该属性对应mongodb的字段的名字，如果一致，则无需该注解
    //@Field("name")
    private String name;
    /**
     * 学生年龄
     */
    private Integer age;
    /**
     * 选修课程列表
     */
    private List<CourseInfo> chooseCourse;
    /**
     * 创建时间
     */
    private LocalDateTime addTime;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
    /**
     * 删除标记
     */
    private Boolean deleted;
}

