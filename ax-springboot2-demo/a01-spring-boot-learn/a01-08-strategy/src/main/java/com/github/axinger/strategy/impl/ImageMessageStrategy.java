package com.github.axinger.strategy.impl;

import com.github.axinger.annotation.MsgTypeHandler;
import com.github.axinger.entity.MessageInfo;
import com.github.axinger.entity.MsgTypeEnum;
import com.github.axinger.strategy.MessageStrategy;
import org.springframework.stereotype.Service;

@Service
@MsgTypeHandler(MsgTypeEnum.IMAGE)
public class ImageMessageStrategy implements MessageStrategy {

    @Override
    public void handleMessage(MessageInfo messageInfo) {
        System.out.println("处理图片消息 = " + messageInfo.getContent());
    }
}
