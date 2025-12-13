package com.github.axinger.dto;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;

import java.lang.reflect.Type;

public class GenderObjectWriter implements ObjectWriter<Gender> {
    @Override
    public void write(JSONWriter jsonWriter, Object object, Object fieldName, Type fieldType, long features) {
        Gender gender = (Gender) object;
        if (gender == null) {
            jsonWriter.writeNull();
        } else {
            // ✅ 选择你想要的输出格式：
//            jsonWriter.writeInt32(gender.getCode());        // 输出 code: 1,2,3
            jsonWriter.writeString(gender.getDesc());     // 输出 desc: "男","女"
        }
    }
}
