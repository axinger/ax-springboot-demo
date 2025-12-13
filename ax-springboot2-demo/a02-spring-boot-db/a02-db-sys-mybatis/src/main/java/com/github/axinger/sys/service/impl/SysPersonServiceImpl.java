package com.github.axinger.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.axinger.sys.domain.SysPersonEntity;
import com.github.axinger.sys.mapper.SysPersonMapper;
import com.github.axinger.sys.service.SysPersonService;
import org.springframework.stereotype.Service;

/**
 * @author cepai
 * @description 针对表【person】的数据库操作Service实现
 * @createDate 2023-10-31 11:20:21
 */
@Service
public class SysPersonServiceImpl extends ServiceImpl<SysPersonMapper, SysPersonEntity>
        implements SysPersonService {

}




