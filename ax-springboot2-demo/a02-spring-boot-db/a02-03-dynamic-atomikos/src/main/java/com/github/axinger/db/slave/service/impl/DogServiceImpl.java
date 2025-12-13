package com.github.axinger.db.slave.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.axinger.db.slave.domain.Dog;
import com.github.axinger.db.slave.mapper.DogMapper;
import com.github.axinger.db.slave.service.DogService;
import org.springframework.stereotype.Service;

/**
 * @author xing
 * @description 针对表【t_dog】的数据库操作Service实现
 * @createDate 2024-07-30 18:14:48
 */
@Service
public class DogServiceImpl extends ServiceImpl<DogMapper, Dog>
        implements DogService {

}




