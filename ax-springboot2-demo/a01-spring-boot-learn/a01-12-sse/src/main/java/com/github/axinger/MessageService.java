package com.github.axinger;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
public class MessageService {
    private final Sinks.Many<Message> messageSink;

    public MessageService(Sinks.Many<Message> messageSink) {
        this.messageSink = messageSink;
    }

  /*
  解释：

tryEmitNext: 尝试发送一个非空元素，生成一个 onNext 信号。此次尝试的结果会以 EmitResult 的形式表示，该结果可能指示出错误情况。
asFlux: 返回此 Sink 的一个 Flux 视图。每次调用都会返回同一个实例。
   */

    public Mono<Message> saveMessage(Mono<Message> message) {
        return message.doOnNext(messageSink::tryEmitNext);
    }

    public Flux<Message> messageStream() {
        return messageSink.asFlux();
    }
}
