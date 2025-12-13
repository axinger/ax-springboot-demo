package com.github.axinger.dao;

import com.github.axinger.entity.SysEmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

// Repository
public interface SysEmployeeDTO extends JpaRepository<SysEmployeeEntity, Long> {


}
