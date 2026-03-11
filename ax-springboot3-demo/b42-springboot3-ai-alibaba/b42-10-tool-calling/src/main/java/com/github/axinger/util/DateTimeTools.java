package com.github.axinger.util;

import cn.hutool.core.date.LocalDateTimeUtil;
import org.springframework.ai.tool.annotation.Tool;

import java.time.LocalDateTime;

public class DateTimeTools {

    /**
     * 获取当前时间
     * returnDirect 是否直接返回
     * true:直接返回结果,不走大模型，直接给客户,直接是纯文本结果
     * false: 拿到tool返回的结果，给大模型，最后由大模型回复，会加工添加说明
     */
    @Tool(description = "获取当前时间", returnDirect = false)
    public String getCurrentTime() {
        return "当前时间是：" + LocalDateTimeUtil.format(LocalDateTime.now(), "yyyy-MM-dd HH:mm:ss");
    }
}
