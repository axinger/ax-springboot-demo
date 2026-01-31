package com.github.axinger.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.axinger.domain.PersonEntity;
import com.github.axinger.injector.MyBaseMapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.session.ResultHandler;

/**
 * @author cepai
 * @description 针对表【person】的数据库操作Mapper
 * @createDate 2023-10-31 11:20:21
 * @Entity com.github.axinger.domain.Person
 */
public interface PersonMapper extends MyBaseMapper<PersonEntity> {

    IPage<PersonEntity> customSqlSegment(@Param(Constants.WRAPPER) Wrapper<PersonEntity> wrapper, Page<Object> page);


    IPage<PersonEntity> sqlSegment(@Param(Constants.WRAPPER) Wrapper<PersonEntity> wrapper, Page<Object> page);

    @Select("select * from book")
    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 500)
    @ResultType(PersonEntity.class)
    void streamSelect(ResultHandler<PersonEntity> handler);


    @Select("select * from book t ${ew.customSqlSegment}")
    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 2)
    @ResultType(PersonEntity.class)
    void getBigData(@Param(Constants.WRAPPER) QueryWrapper<PersonEntity> wrapper, ResultHandler<PersonEntity> handler);
}




