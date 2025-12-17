package com.github.axinger.service.impl;

import com.github.axinger.entity.Department;
import com.github.axinger.entity.User;
import com.github.axinger.repository.DepartmentRepository;
import com.github.axinger.repository.UserRepository;
import com.github.axinger.service.OrganizationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrganizationServiceImpl implements OrganizationService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    public OrganizationServiceImpl(UserRepository userRepository, DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public User getDepartmentManager(Long departmentId) {
        // 优先查找本部门的经理
        Optional<User> manager = userRepository.findDepartmentManager(departmentId);
        if (manager.isPresent()) {
            return manager.get();
        }

        // 查找上级部门的经理逻辑已移除，因为现在没有直接的父子部门关系

        // 如果还没有，查找总经理
        return getGeneralManager();
    }

    @Override
    public User getFinanceApprover() {
        return userRepository.findFinanceApprover().orElseGet(this::getGeneralManager);
    }

    @Override
    public User getViceGeneralManager() {
        return userRepository.findViceGeneralManager().orElseGet(this::getGeneralManager);
    }

    @Override
    public User getGeneralManager() {
        return userRepository.findGeneralManager().orElse(null);
    }

    @Override
    public List<User> getDepartmentMembers(Long departmentId) {
        return userRepository.findByDepartmentId(departmentId);
    }

    @Override
    public Department getDepartmentById(Long departmentId) {
        return departmentRepository.findById(departmentId).orElse(null);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
