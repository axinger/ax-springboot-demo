package com.github.axinger.sys.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.axinger.sys.domain.SysUserEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author xing
 * @description 针对表【sys_user】的数据库操作Mapper
 * @createDate 2025-03-15 15:50:07
 * @Entity com.github.axinger.domain.SysUserEntity
 */
public interface SysUserMapper extends BaseMapper<SysUserEntity> {

    int insertOrUpdateOnDuplicate(SysUserEntity users);

    /**
     * username和email均有唯一索引，任意一个重复均会触发更新,
     */
    int batchInsertOrUpdateOnDuplicate(@Param("list") List<SysUserEntity> users);


    SysUserEntity getUserByMap(Map<String, Object> params);

    SysUserEntity getUserByIdAndName(@Param("username") String username);

    SysUserEntity getUserByQuery(SysUserEntity query);

    /// 自定义分页
    Page<SysUserEntity> conditionalPage(@Param("page") IPage<SysUserEntity> page, @Param(Constants.WRAPPER) Wrapper<SysUserEntity> wrapper);

}




