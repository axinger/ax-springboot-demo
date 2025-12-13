package com.xing.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * @Description：课程信息实体类
 * @Author :zks
 * @Date :9:29 2020/9/15
 */
@Data// lombok插件
@Document(collection = "courseInfo")// 可以省略，如果省略，则默认使用类名小写映射集合
public class CourseInfo {
    // 主键标识，该属性的值会自动对应mongodb的主键字段"_id"，如果该属性名就叫“id”,则该注解可以省略，否则必须写
    @Id
    private ObjectId id;
    /**
     * 课程id
     */
    // 该属性对应mongodb的字段的名字，如果一致，则无需该注解
    //@Field("courseId")
    private Integer courseId;
    /**
     * 课程名称
     */
    private String Name;
    /**
     * 课程描述
     */
    private String describe;
    /**
     * 创建时间
     */
    private LocalDateTime addTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 删除标记
     */
    private Boolean deleted;

}

