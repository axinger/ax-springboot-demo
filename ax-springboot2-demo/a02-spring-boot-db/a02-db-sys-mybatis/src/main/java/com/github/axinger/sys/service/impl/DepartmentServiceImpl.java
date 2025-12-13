package com.github.axinger.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.axinger.sys.domain.DepartmentEntity;
import com.github.axinger.sys.mapper.DepartmentMapper;
import com.github.axinger.sys.service.DepartmentService;
import org.springframework.stereotype.Service;

/**
 * @author xing
 * @description 针对表【department】的数据库操作Service实现
 * @createDate 2022-12-17 19:55:51
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, DepartmentEntity>
        implements DepartmentService {

}




