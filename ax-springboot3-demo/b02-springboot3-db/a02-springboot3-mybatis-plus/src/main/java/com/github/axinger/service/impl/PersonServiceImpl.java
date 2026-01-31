package com.github.axinger.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.axinger.domain.PersonEntity;
import com.github.axinger.mapper.PersonMapper;
import com.github.axinger.service.PersonService;
import org.springframework.stereotype.Service;

/**
 * @author cepai
 * @description 针对表【person】的数据库操作Service实现
 * @createDate 2023-10-31 11:20:21
 */
@Service
public class PersonServiceImpl extends ServiceImpl<PersonMapper, PersonEntity>
        implements PersonService {

}




