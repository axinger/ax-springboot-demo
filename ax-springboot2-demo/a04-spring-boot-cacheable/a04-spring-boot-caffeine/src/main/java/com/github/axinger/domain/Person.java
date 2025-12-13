package com.github.axinger.domain;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @TableName t_student
 */
@Data
@Builder
public class Person implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private Integer age;
}
