package com.github.axinger;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.axinger.domain.EmployeeEntity;
import com.github.axinger.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class EmployeeEntityServiceTest {

    @Autowired
    private EmployeeService employeeService;

    @Test
    void test_list() {
        List<EmployeeEntity> list = employeeService.list();
        System.out.println("list = " + list);
    }

    @Test
    void test_list2() {
        List<EmployeeEntity> list = employeeService.leftDepartmentList(Wrappers.<EmployeeEntity>lambdaQuery());
        System.out.println("list = " + list);
    }

    @Test
    void test6_2() {
        List<EmployeeEntity> list = employeeService.leftDepartmentList(Wrappers.<EmployeeEntity>lambdaQuery()
                .eq(EmployeeEntity::getDeptId, 1)
                .last("limit 1")
        );
        System.out.println("list = " + list);
    }
}
