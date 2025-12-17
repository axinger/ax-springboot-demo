package com.github.axinger.service;

import com.github.axinger.entity.Department;
import com.github.axinger.entity.User;

import java.util.List;

public interface OrganizationService {

    User getUserById(Long userId);

    User getDepartmentManager(Long departmentId);

    User getFinanceApprover();

    User getViceGeneralManager();

    User getGeneralManager();

    List<User> getDepartmentMembers(Long departmentId);

    Department getDepartmentById(Long departmentId);

    User getUserByUsername(String username);

    List<Department> getAllDepartments();

    List<User> getAllUsers();
}
