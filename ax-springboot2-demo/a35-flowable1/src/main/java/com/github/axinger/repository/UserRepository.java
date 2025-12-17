package com.github.axinger.repository;

import com.github.axinger.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.departmentId = :departmentId AND u.position LIKE %:position%")
    List<User> findUsersByDepartmentAndPosition(
        @Param("departmentId") Long departmentId,
        @Param("position") String position
    );

    @Query("SELECT u FROM User u WHERE u.position LIKE %:position%")
    List<User> findUsersByPosition(@Param("position") String position);

    default Optional<User> findDepartmentManager(Long departmentId) {
        List<User> managers = findUsersByDepartmentAndPosition(departmentId, "经理");
        if (managers.isEmpty()) {
            managers = findUsersByDepartmentAndPosition(departmentId, "主管");
        }
        return managers.stream().findFirst();
    }

    default Optional<User> findFinanceApprover() {
        List<User> approvers = findUsersByPosition("财务审批人");
        if (approvers.isEmpty()) {
            approvers = findUsersByPosition("财务");
        }
        return approvers.stream().findFirst();
    }

    default Optional<User> findViceGeneralManager() {
        List<User> vgmList = findUsersByPosition("副总经理");
        if (vgmList.isEmpty()) {
            vgmList = findUsersByPosition("副总");
        }
        return vgmList.stream().findFirst();
    }

    default Optional<User> findGeneralManager() {
        List<User> gmList = findUsersByPosition("总经理");
        if (gmList.isEmpty()) {
            gmList = findUsersByPosition("总经");
        }
        return gmList.stream().findFirst();
    }

    List<User> findByDepartmentId(Long departmentId);
}