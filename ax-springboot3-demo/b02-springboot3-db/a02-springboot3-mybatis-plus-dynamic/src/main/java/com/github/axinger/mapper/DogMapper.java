package com.github.axinger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.axinger.annotation.DS_SUB;
import com.github.axinger.domain.Dog;

/**
 * @author xing
 * @description 针对表【t_dog】的数据库操作Mapper
 * @createDate 2024-07-30 18:14:48
 * @Entity com.github.axinger.domain.Dog
 */
@DS_SUB
public interface DogMapper extends BaseMapper<Dog> {

}




