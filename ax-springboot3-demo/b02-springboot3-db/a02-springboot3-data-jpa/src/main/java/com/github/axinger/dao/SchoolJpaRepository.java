package com.github.axinger.dao;

import com.github.axinger.model.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolJpaRepository extends JpaRepository<School, Integer> {
}
