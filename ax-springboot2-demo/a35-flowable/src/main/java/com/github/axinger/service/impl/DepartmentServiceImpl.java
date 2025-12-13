package com.github.axinger.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.axinger.domain.DepartmentEntity;
import com.github.axinger.mapper.DepartmentMapper;
import com.github.axinger.service.DepartmentService;
import org.springframework.stereotype.Service;

/**
 * @author xing
 * @description 针对表【department】的数据库操作Service实现
 * @createDate 2025-05-24 13:28:16
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, DepartmentEntity>
        implements DepartmentService {

}




