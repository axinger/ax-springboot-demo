package com.github.axinger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.axinger.domain.SysAnimalEntity;
import org.apache.ibatis.annotations.Select;

/**
 * @author xing
 * @description 针对表【sys_animal】的数据库操作Mapper
 * @createDate 2024-12-28 20:42:40
 * @Entity com.github.axinger.db.master.domain.SysAnimalEntity
 */
public interface SysAnimalMapper extends BaseMapper<SysAnimalEntity> {


    @Select("TRUNCATE TABLE sys_animal;")
    void truncate();
}




