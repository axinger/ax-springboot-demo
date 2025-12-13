package com.github.axinger.db.master.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.axinger.db.master.domain.Person;
import com.github.axinger.db.master.mapper.PersonMapper;
import com.github.axinger.db.master.service.PersonService;
import org.springframework.stereotype.Service;

/**
 * @author xing
 * @description 针对表【t_person】的数据库操作Service实现
 * @createDate 2024-07-30 18:14:38
 */
@Service
public class PersonServiceImpl extends ServiceImpl<PersonMapper, Person>
        implements PersonService {

}




