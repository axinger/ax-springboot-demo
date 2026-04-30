package com.github.axinger.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.axinger.entity.SysMessage;
import com.github.axinger.mapper.SysMessageMapper;
import com.github.axinger.service.SysMessageService;
import org.springframework.stereotype.Service;

/**
 * 消息服务实现类
 */
@Service
public class SysMessageServiceImpl extends ServiceImpl<SysMessageMapper, SysMessage> implements SysMessageService {
}
