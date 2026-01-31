package com.github.axinger.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.axinger.annotation.DS_SUB;
import com.github.axinger.domain.Dog;
import com.github.axinger.mapper.DogMapper;
import com.github.axinger.service.DogService;
import org.springframework.stereotype.Service;

/**
 * @author xing
 * @description 针对表【t_dog】的数据库操作Service实现
 * @createDate 2024-07-30 18:14:48
 */
@Service
@DS_SUB
public class DogServiceImpl extends ServiceImpl<DogMapper, Dog>
        implements DogService {

}




