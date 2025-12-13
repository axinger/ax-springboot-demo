package com.github.axinger.strategy;

import com.github.axinger.entity.MessageInfo;

public interface MessageStrategy {

    void handleMessage(MessageInfo messageInfo);
}
