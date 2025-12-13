package com.github.axinger.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.axinger.sys.domain.EmployeeEntity;
import com.github.axinger.sys.mapper.EmployeeMapper;
import com.github.axinger.sys.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeMapper employeeMapper;

    @Test
    public void add() {
        employeeService.save(EmployeeEntity.builder().name("jim").build());
    }

    @Test
    void test_list() {
        List<EmployeeEntity> list = employeeService.list();
        System.out.println("list = " + list);
    }

    @Test
    void test_list2() {
        List<EmployeeEntity> list = employeeMapper.leftDepartment(Wrappers.<EmployeeEntity>lambdaQuery()
                .eq(EmployeeEntity::getDeptId, 1));
        System.out.println("list = " + list);
    }

    @Test
    void test6_2() {
        List<EmployeeEntity> list = employeeMapper.leftDepartment(Wrappers.<EmployeeEntity>lambdaQuery()
                .eq(EmployeeEntity::getDeptId, 1)
                .last("limit 1")
        );
        System.out.println("list = " + list);
    }
}
