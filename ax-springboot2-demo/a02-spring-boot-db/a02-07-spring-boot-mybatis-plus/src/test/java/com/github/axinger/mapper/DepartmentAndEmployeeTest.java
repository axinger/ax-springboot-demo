package com.github.axinger.mapper;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.axinger.sys.domain.DepartmentEntity;
import com.github.axinger.sys.domain.EmployeeEntity;
import com.github.axinger.sys.mapper.DepartmentMapper;
import com.github.axinger.sys.mapper.EmployeeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class DepartmentAndEmployeeTest {

    @Autowired
    DepartmentMapper departmentMapper;


    @Autowired
    private EmployeeMapper employeeMapper;

    @Test
    void test_save_Employee() {

        String emStr = """
                [
                
                    {
                        "deptId": 1,
                        "email": "",
                        "gender": 1,
                        "id": 1,
                        "name": "jim"
                    }
                    ,{
                        "deptId": 2,
                        "email": "",
                        "gender": 1,
                        "id": 2,
                        "name": "tom"
                    },
                    {
                        "deptId": 11,
                        "email": "",
                        "gender": 2,
                        "id": 3,
                        "name": "lili"
                    }
                ]
                """;

        String depStr = """
                [
                    {
                        "id": 1,
                        "name": "财务部"
                    },
                    {
                        "id": 2,
                        "name": "研发部"
                    },
                    {
                        "id": 3,
                        "name": "销售部"
                    }
                ]
                """;

        employeeMapper.insert(JSON.parseArray(emStr, EmployeeEntity.class));
        departmentMapper.insert(JSON.parseArray(depStr, DepartmentEntity.class));
    }

    @Test
    void test_Department_departByEmployeeList_不可以() {
        // List<Department> list = departmentService.getDepartmentByEmployee(Wrappers.<Department>lambdaQuery()
        //         .eq(Department::getId, 1));

        List<DepartmentEntity> list = departmentMapper.departByEmployeeList(Wrappers.<DepartmentEntity>query()
                .eq("d.id", 1));
        System.out.println("list = " + list);

    }

    @Test
    void test_Department_listLeft_可以() {
        List<DepartmentEntity> list1 = departmentMapper.listLeft(Wrappers.<DepartmentEntity>query()
                .eq("d.id", 1));
        System.out.println("list1 = " + list1);
    }

    @Test
    void test_Department_listLeft_不可以() {

        // 不可以
        List<DepartmentEntity> list2 = departmentMapper.listLeft(Wrappers.<DepartmentEntity>lambdaQuery()
                .eq(DepartmentEntity::getId, 1));
        System.out.println("list = " + list2);
    }


    /**
     * 查询指定部门所有人
     */
    @Test
    void test_Department_listLeftSon_可以() {

        List<DepartmentEntity> list = departmentMapper.listLeftSon(Wrappers.lambdaQuery());
        System.out.println("list = " + list);

        // 可以
        List<DepartmentEntity> list2 = departmentMapper.listLeftSon(Wrappers.<DepartmentEntity>lambdaQuery()
                .eq(DepartmentEntity::getId, 1)

        );
        System.out.println("list2 = " + list2);

    }

    @Test
    void test_Department_listAllSon_不可以() {
        // 不可以
        LambdaQueryWrapper<DepartmentEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DepartmentEntity::getId, 1);
        List<DepartmentEntity> list2 = departmentMapper.listWhere(wrapper);
        System.out.println("list = " + list2);
    }

    @Test
    void test7_Department_listAllSon_可以() {
        // 可以
        QueryWrapper<DepartmentEntity> wrapper = Wrappers.<DepartmentEntity>query()
                .eq("d.id", 1);
        List<DepartmentEntity> list2 = departmentMapper.listWhere(wrapper);
        System.out.println("list = " + list2);
    }
}
