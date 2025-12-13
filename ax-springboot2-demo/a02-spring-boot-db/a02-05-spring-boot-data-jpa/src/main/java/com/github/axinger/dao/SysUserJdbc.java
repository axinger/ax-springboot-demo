package com.github.axinger.dao;

import com.github.axinger.entity.SysUser;
import org.springframework.data.repository.CrudRepository;

public interface SysUserJdbc extends CrudRepository<SysUser, Long> {
}
