package com.github.axinger.strategy.impl;

import com.github.axinger.annotation.MsgTypeHandler;
import com.github.axinger.entity.MessageInfo;
import com.github.axinger.entity.MsgTypeEnum;
import com.github.axinger.strategy.MessageStrategy;
import org.springframework.stereotype.Service;

@Service
@MsgTypeHandler(value = MsgTypeEnum.TEXT)
public class TextMessageStrategy implements MessageStrategy {

    @Override
    public void handleMessage(MessageInfo messageInfo) {
        System.out.println("处理文本消息 = " + messageInfo.getContent());
    }
}
