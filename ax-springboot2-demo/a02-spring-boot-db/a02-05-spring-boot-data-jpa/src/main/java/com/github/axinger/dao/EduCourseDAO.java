package com.github.axinger.dao;

import com.github.axinger.entity.EduCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EduCourseDAO extends JpaRepository<EduCourse, Integer> {
}

