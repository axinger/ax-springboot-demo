package com.xing.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName Student.java
 * @description TODO
 * @createTime 2022年06月13日 10:37:00
 */
@Data
@Document
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private Integer id;
    private String username;
    private Integer classId;

}
