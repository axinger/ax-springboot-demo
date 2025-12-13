package com.github.axinger.dao;

import com.github.axinger.model.Person;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Repository接口
 * 提供了方法名称命名查询方式
 * 提供了基于@Query注解查询与更新
 */
public interface UsersRepository extends Repository<Person, Integer> {

    List<Person> findAll();

}
