package com.github.axinger.service;

import com.github.axinger.entity.MessageInfo;

public interface MessageService {

    void handleMessage(MessageInfo messageInfo);
}
