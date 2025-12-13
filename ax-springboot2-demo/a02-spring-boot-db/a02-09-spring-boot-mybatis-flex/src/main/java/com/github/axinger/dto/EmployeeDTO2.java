package com.github.axinger.dto;

import com.github.axinger.model.Gender;
import lombok.Data;

/**
 * 实体类。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@Data
public class EmployeeDTO2 {

    private Long id;

    private Integer deptId;

    private String email;

    private Gender gender;

    private String name;

    /**
     * 部门DTO, dto需要Table注解,用来自动映射
     */
    private DepartmentDTO dept;
//    private Department dept;

}
