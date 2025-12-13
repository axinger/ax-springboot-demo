package com.github.axinger.service.impl;

import com.github.axinger.annotation.MsgTypeHandler;
import com.github.axinger.entity.MessageInfo;
import com.github.axinger.entity.MsgTypeEnum;
import com.github.axinger.service.MessageService;
import com.github.axinger.strategy.MessageStrategy;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName MessageServiceImpl.java
 * @description TODO
 * @createTime 2022年07月30日 13:46:00
 */
@Service
public class MessageServiceImpl implements MessageService {


    private Map<MsgTypeEnum, MessageStrategy> messageStrategyMap;

    @Resource
    private void setMessageStrategyMap(List<MessageStrategy> messageStrategies) {
//        messageStrategyMap = messageStrategies.stream().collect(
//                Collectors.toMap(item -> AnnotationUtils.findAnnotation(item.getClass(), MsgTypeHandler.class).value(), v -> v));

        messageStrategyMap = messageStrategies.stream().collect(
                Collectors.toMap(item -> Objects.requireNonNull(AnnotationUtils.findAnnotation(item.getClass(), MsgTypeHandler.class)).value(), v -> v));
    }


    @Override
    public void handleMessage(MessageInfo messageInfo) {
        MessageStrategy messageStrategy = messageStrategyMap.get(messageInfo.getType());
        messageStrategy.handleMessage(messageInfo);
    }
}
