package com.github.axinger.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.axinger.domain.Student;
import com.github.axinger.mapper.StudentMapper;
import com.github.axinger.service.StudentService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * @author xing
 * @description 针对表【t_student】的数据库操作Service实现
 * @createDate 2023-01-06 11:35:44
 */
@Service
@CacheConfig(cacheNames = "st")
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student>
        implements StudentService {

    // @Cacheable(key = "#id")
    // @Override
    // public Student getById(Serializable id) {
    //     return StudentService.super.getById(id);
    // }
    //
    // @Override
    // public List<Student> list() {
    //     return StudentService.super.list();
    // }
}




