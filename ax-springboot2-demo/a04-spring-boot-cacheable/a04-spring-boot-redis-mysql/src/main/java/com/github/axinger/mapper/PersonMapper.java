package com.github.axinger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.axinger.domain.PersonEntity;
import org.apache.ibatis.annotations.CacheNamespace;

/**
 * @author cepai
 * @description 针对表【person】的数据库操作Mapper
 * @createDate 2023-12-19 14:55:09
 * @Entity com.github.axinger.redis.domain.PersonEntity
 */
//@CacheNamespace()
@CacheNamespace
public interface PersonMapper extends BaseMapper<PersonEntity> {

}




