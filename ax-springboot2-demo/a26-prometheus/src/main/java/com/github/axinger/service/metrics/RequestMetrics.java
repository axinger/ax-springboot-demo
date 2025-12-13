package com.github.axinger.service.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RequestMetrics {
    private final Timer apiRequestTimer;
    private final Timer dbQueryTimer;
    private final MeterRegistry registry;

    public RequestMetrics(MeterRegistry registry) {
        this.registry = registry;  // 初始化registry

        this.apiRequestTimer = Timer.builder("api.request.duration")
                .description("api.请求时长,描述....")
                .publishPercentiles(0.5, 0.95)
                .register(registry);

        this.dbQueryTimer = Timer.builder("db.query.duration")
                .description("db.查询时长,描述...")
                .publishPercentileHistogram()
                .register(registry);
    }

    public Timer.Sample startApiRequestTimer() {
        return Timer.start(registry);  // 使用保存的registry
    }

    public void stopApiRequestTimer(Timer.Sample sample, String endpoint) {
        // 通过工厂函数获取带有特定endpoint标签的Timer实例
        sample.stop(apiRequestTimer);
    }

    public void recordDbQueryTime(Runnable query) {
        dbQueryTimer.record(query);
    }

    public void recordDbQueryTime(long duration, TimeUnit unit) {
        dbQueryTimer.record(duration, unit);
    }
}
