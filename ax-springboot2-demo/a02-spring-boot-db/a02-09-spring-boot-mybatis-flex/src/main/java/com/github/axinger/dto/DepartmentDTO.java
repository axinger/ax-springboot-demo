package com.github.axinger.dto;

import com.mybatisflex.annotation.Table;
import lombok.Data;

/**
 * 实体类。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@Data
/*
  dto 加Table ,用来自动映射
 */
@Table(value = "department")
public class DepartmentDTO {

    private Long id;

    private String name;
}
