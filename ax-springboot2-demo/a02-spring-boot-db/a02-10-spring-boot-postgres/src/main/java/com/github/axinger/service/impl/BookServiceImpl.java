package com.github.axinger.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.axinger.domain.BookEntity;
import com.github.axinger.mapper.BookMapper;
import com.github.axinger.service.BookService;
import org.springframework.stereotype.Service;

/**
 * @author xing
 * @description 针对表【book】的数据库操作Service实现
 * @createDate 2023-01-12 11:43:26
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, BookEntity>
        implements BookService {

}




