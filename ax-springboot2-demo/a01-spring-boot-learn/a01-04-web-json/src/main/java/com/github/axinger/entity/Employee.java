package com.github.axinger.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("employeeFilter") // 放这里有可以
public class Employee {
    private String id;
    private String name;
    private BigDecimal salary;
}
