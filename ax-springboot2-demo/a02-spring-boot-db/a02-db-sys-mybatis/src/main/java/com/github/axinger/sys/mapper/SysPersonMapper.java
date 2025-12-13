package com.github.axinger.sys.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.axinger.sys.domain.SysPersonEntity;
import com.github.axinger.sys.injector.MyBaseMapper;
import com.github.axinger.sys.vo.SysPersonVO;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.session.ResultHandler;

import java.util.List;

/**
 * @author cepai
 * @description 针对表【person】的数据库操作Mapper
 * @createDate 2023-10-31 11:20:21
 * @Entity com.github.axinger.domain.Person  BaseMapper   MyBaseMapper
 */
public interface SysPersonMapper extends MyBaseMapper<SysPersonEntity> {

    /**
     * ${ew.sqlSegment}， SQL片段
     * <p>
     * ${ew.customSqlSegment}, 传统SQL片段。常用
     * <p>
     * ${ew.sqlSelect}， SQL选择
     * <p>
     * ${ew.sqlSet}使用 SQL设置
     */


    IPage<SysPersonEntity> customSqlSegment(@Param(Constants.WRAPPER) Wrapper<SysPersonEntity> wrapper, Page<Object> page);


    IPage<SysPersonEntity> sqlSegment(@Param(Constants.WRAPPER) Wrapper<SysPersonEntity> wrapper, Page<Object> page);

    /**
     * @param handler handler
     * @Select mybatis方法 语法
     */
    @Select("select * from sys_person")
    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 20)
    @ResultType(SysPersonEntity.class)
    void selectStream(ResultHandler<SysPersonEntity> handler);

    ///  ${ew.customSqlSegment} 会直接在前面添加 where
    @Select("select * from sys_person ${ew.customSqlSegment}")
    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 20)
    @ResultType(SysPersonEntity.class)
    void selectStreamWrapper(@Param(Constants.WRAPPER) Wrapper<SysPersonEntity> wrapper, ResultHandler<SysPersonEntity> handler);

    /// mybatis方法
    @Select("select * from sys_person")
    /// 关键点是设置 fetchSize=Integer.MIN_VALUE（即 -2147483648），这会启用 MySQL 的流式结果集。
    /// 对于 H2，设置 fetchSize=1 可以实现类似流式的效果
    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = Integer.MIN_VALUE)
    @ResultType(SysPersonEntity.class)
    Cursor<SysPersonEntity> cursorSelect();


    @Select("select * from sys_person")
    List<SysPersonVO> selectListWihVO();


    ///  1、${ew.customSqlSegment} 会直接在前面添加 where
    @Select("select * from sys_person ${ew.customSqlSegment}")
    List<SysPersonEntity> getCustomSqlSegment(@Param(Constants.WRAPPER) Wrapper<SysPersonEntity> queryWrapper);


    ///  2、${ew.sqlSegment} 就只有条件语句
    @Select("select * from sys_person where ${ew.sqlSegment}")
    List<SysPersonEntity> getSqlSegment(@Param(Constants.WRAPPER) Wrapper<SysPersonEntity> queryWrapper);


    ///  3、${ew.sqlSelect} 就是 queryWrapper.select(****) 你所定义需要查询的字段
    @Select("select ${ew.sqlSelect} from sys_person ")
    List<SysPersonEntity> getSqlSelect(@Param(Constants.WRAPPER) Wrapper<SysPersonEntity> queryWrapper);
}




