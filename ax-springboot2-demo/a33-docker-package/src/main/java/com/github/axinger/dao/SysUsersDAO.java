package com.github.axinger.dao;

import com.github.axinger.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository接口
 * 提供了方法名称命名查询方式
 * 提供了基于@Query注解查询与更新
 */
public interface SysUsersDAO extends JpaRepository<SysUser, Integer> {


}
