package com.github.axinger.service.impl;


import com.github.axinger.domain.Person;
import com.github.axinger.mapper.PersonMapper;
import com.github.axinger.service.PersonService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 服务层实现。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@Service
public class PersonServiceImpl extends ServiceImpl<PersonMapper, Person> implements PersonService {

}
