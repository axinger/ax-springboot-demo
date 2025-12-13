package com.github.axinger.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("departmentFilter")
public class Department {
    private String id;
    private String name;


    @JsonFilter("employeeFilter") //这里也可以
    private List<Employee> employees;
}

