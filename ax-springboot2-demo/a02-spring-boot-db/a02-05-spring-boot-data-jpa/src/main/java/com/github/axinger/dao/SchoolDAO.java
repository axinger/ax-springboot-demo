package com.github.axinger.dao;

import com.github.axinger.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolDAO extends JpaRepository<School, Integer> {
}
