package com.github.axinger.db2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.axinger.annotation.DB2;
import com.github.axinger.db2.domain.SysAnimalEntity;

/**
 * @author xing
 * @description 针对表【sys_animal】的数据库操作Mapper
 * @createDate 2025-01-07 23:05:13
 * @Entity com.github.axinger.db2.domain.SysAnimalEntity
 */
@DB2
public interface SysAnimalMapper extends BaseMapper<SysAnimalEntity> {

}




