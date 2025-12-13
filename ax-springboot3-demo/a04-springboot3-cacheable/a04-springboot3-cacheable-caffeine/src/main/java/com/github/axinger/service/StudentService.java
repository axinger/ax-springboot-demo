package com.github.axinger.service;

import com.github.axinger.domain.Student;
import org.springframework.cache.annotation.Cacheable;

/**
 * @author xing
 * @description 针对表【t_student】的数据库操作Service
 * @createDate 2023-01-06 11:35:44
 */
@Cacheable(value = "sc")
public interface StudentService extends IServiceCacheable<Student> {

}
