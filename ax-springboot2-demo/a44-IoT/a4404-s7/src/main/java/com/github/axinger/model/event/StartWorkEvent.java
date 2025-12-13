package com.github.axinger.model.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class StartWorkEvent extends ApplicationEvent {

    private final String projectId;

    public StartWorkEvent(String source) {
        super(source);
        this.projectId = source;
    }
}
