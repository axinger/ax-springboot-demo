package com.github.axinger.jackson;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Data;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@Data
public class SysPerson2 {
    private String name;
    private int age;


    @JsonGetter("name")
    public String getFullName() {
        return name+"全名称";
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#.##") ///不能格式化数字
    private BigDecimal add;

    @JsonGetter("add") ///  覆盖原有的
    public String getAddFormatted() {
        if (add == null) {
            return null;
        }
        DecimalFormat df = new DecimalFormat("#.##");
//        return df.format(add);
        return add.toPlainString();
    }

}
