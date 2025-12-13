package com.github.axinger.service.impl;

import com.github.axinger.domain.DepartmentEntity;
import com.github.axinger.domain.EmployeeEntity;
import com.github.axinger.service.DepartmentService;
import com.github.axinger.service.EmployeeService;
import com.github.axinger.service.OrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrgServiceImpl implements OrgService {

    @Autowired
    private EmployeeService employeeRepository;

    @Autowired
    private DepartmentService departmentRepository;

    @Override
    public String getDirectLeader(String employeeId) {
        EmployeeEntity employee = employeeRepository.getById(employeeId);
        return employee.getDirectLeaderId();
    }

    @Override
    public String getDeptLeader(String employeeId) {
        EmployeeEntity employee = employeeRepository.getById(employeeId);
        DepartmentEntity dept = departmentRepository.getById(employee.getDepartmentId());
        return dept.getDeptLeaderId();
    }

    @Override
    public String getCompanyLeader() {
        // 假设公司只有一个最高领导
        return employeeRepository.lambdaQuery()
                .eq(EmployeeEntity::getPosition, "CEO")
                .last("limit 1")
                .one().getId();
    }
}
