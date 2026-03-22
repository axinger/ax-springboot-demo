package com.github.axinger.config;

import com.alibaba.ttl.TtlRunnable;
import org.jspecify.annotations.NonNull;
import org.springframework.core.task.TaskDecorator;

public class TtlTaskDecorator implements TaskDecorator {
    @Override
    public @NonNull Runnable decorate(@NonNull Runnable runnable) {
        // 使用 TtlRunnable 包装原始任务，实现上下文传递
        return TtlRunnable.get(runnable);
    }
}
