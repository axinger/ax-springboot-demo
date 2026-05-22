package com.github.axinger.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.axinger.domain.School;
import com.github.axinger.mapper.SchoolMapper;
import com.github.axinger.service.SchoolService;
import org.springframework.stereotype.Service;

/**
 * @author xing
 * @description 针对表【t_school】的数据库操作Service实现
 * @createDate 2023-01-06 12:08:40
 */
@Service
public class SchoolServiceImpl extends ServiceImpl<SchoolMapper, School>
        implements SchoolService {

}




