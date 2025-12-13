package com.github.axinger.sse;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class ClientManager {
    private final ConcurrentMap<String, Sinks.Many<String>> clientSinks = new ConcurrentHashMap<>();

    public Sinks.Many<String> getSink(String clientId) {
        return clientSinks.computeIfAbsent(clientId, id -> Sinks.many().multicast().onBackpressureBuffer());
    }

    public void removeSink(String clientId) {
        clientSinks.remove(clientId);
    }
}
