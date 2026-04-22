package com.axing.common.excel.handler;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
public class CommonExcelHandler<T> extends AnalysisEventListener<T> {

    @Getter
    protected final List<String> failList = new ArrayList<>();

    private Consumer<T> consumer;

    private CommonExcelHandler() {
    }

    public CommonExcelHandler(Consumer<T> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        log.info("表头：" + headMap);
    }


    @Override
    public void invoke(T data, AnalysisContext context) {
        log.info("每行invoke = {}", data);
        if (Optional.ofNullable(consumer).isPresent()) {
            consumer.accept(data);
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        log.info("所有数据解析完成");
    }
}
