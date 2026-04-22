package com.axing.common.executor.decorator;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.lang.NonNull;

import java.util.Map;

/**
 * MDC 任务装饰器 - 用于将 MDC 上下文从主线程传递到子线程
 * <p>
 * 在使用 @Async 或 CompletableFuture 时，MDC 上下文默认不会传递到子线程，
 * 导致子线程中的日志缺少 traceId 等信息。使用此装饰器可以解决这个问题。
 *
 * @author xing
 */
public class MdcTaskDecorator implements TaskDecorator {

    @Override
    public @NonNull Runnable decorate(@NonNull Runnable runnable) {
        // 获取主线程的 MDC 上下文
        Map<String, String> contextMap = MDC.getCopyOfContextMap();

        return () -> {
            try {
                // 子线程设置上下文
                if (contextMap != null) {
                    MDC.setContextMap(contextMap);
                }
                runnable.run();
            } finally {
                // 执行完务必清理，防止线程复用导致数据污染
                MDC.clear();
            }
        };
    }
}
