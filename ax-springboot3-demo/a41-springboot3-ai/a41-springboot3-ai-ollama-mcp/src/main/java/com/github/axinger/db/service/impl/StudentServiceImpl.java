package com.github.axinger.db.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.axinger.db.domain.StudentEntity;
import com.github.axinger.db.mapper.StudentMapper;
import com.github.axinger.db.service.StudentService;
import org.springframework.stereotype.Service;

/**
* @author xing
* @description 针对表【student(学生表)】的数据库操作Service实现
* @createDate 2025-12-15 20:45:14
*/
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, StudentEntity>
    implements StudentService{

}




