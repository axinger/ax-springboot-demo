package com.github.axinger.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Service
public class EventService {

    @Autowired
    private Sinks.Many<String> sseSink;

    public void publishEvent(String event) {
        sseSink.tryEmitNext(event).orThrow();
    }
}
