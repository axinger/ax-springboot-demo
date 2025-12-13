package com.github.axinger.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.axinger.entity.FileDetail;
import com.github.axinger.mapper.FileDetailMapper;
import com.github.axinger.service.FilePartDetailService;
import org.springframework.stereotype.Service;


@Service
public class FilePartDetailServiceImpl extends ServiceImpl<FileDetailMapper, FileDetail>
        implements FilePartDetailService {

}
