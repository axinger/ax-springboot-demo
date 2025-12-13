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
public class EmployeeDTO {

    private Long id;

    private Integer deptId;

    private String email;

    private Gender gender;

    private String name;

    /**
     * 部门名称
     */
    private String deptName;

}
