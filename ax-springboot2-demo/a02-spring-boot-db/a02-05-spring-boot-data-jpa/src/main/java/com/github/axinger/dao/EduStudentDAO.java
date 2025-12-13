package com.github.axinger.dao;

import com.github.axinger.entity.EduStudent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EduStudentDAO extends JpaRepository<EduStudent, Integer> {
}
