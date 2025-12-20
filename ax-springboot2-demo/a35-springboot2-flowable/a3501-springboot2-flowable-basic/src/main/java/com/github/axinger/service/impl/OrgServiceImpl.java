package com.github.axinger.service.impl;

import com.github.axinger.domain.A35DepartmentEntity;
import com.github.axinger.domain.A35UserEntity;
import com.github.axinger.service.A35DepartmentService;
import com.github.axinger.service.A35UserService;
import com.github.axinger.service.OrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrgServiceImpl implements OrgService {

    @Autowired
    private A35UserService userService;

    @Autowired
    private A35DepartmentService departmentRepository;

    @Override
    public A35UserEntity getUserByName(String name) {
        A35UserEntity user = userService.lambdaQuery()
                .eq(A35UserEntity::getName, name)
                .last("limit 1")
                .one();
        return user;
    }

    @Override
    public String getDirectLeader(String employeeId) {
        A35UserEntity employee = userService.getById(employeeId);
        return employee.getDirectLeaderId();
    }

    @Override
    public String getDeptLeader(String employeeId) {
        A35UserEntity employee = userService.getById(employeeId);
        A35DepartmentEntity dept = departmentRepository.getById(employee.getDepartmentId());
        return dept.getDeptLeaderId();
    }

    @Override
    public String getCompanyLeader() {
        // 假设公司只有一个最高领导
        return userService.lambdaQuery()
                .eq(A35UserEntity::getPosition, "CEO")
                .last("limit 1")
                .one().getName();
    }
}
