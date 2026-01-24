package com.github.axinger.jackson;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.axinger.jackson.config.BigDecimal2PlacesSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;

@Data
public class SysPerson2 {
    private String name;
    private int age;
    private BigInteger age1;


    @JsonGetter("name")
    public String getFullName() {
        return name+"全名称";
    }

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#.##") ///不能格式化数字

    @JsonSerialize(using = BigDecimal2PlacesSerializer.class)
    private BigDecimal add;

    @JsonSerialize(using = BigDecimal2PlacesSerializer.class)
    private BigDecimal add2;

    @JsonSerialize(using = BigDecimal2PlacesSerializer.class)
    private BigDecimal add3;

//    @JsonGetter("add") ///  覆盖原有的
//    public String getAddFormatted() {
//        if (add == null) {
//            return null;
//        }
//        DecimalFormat df = new DecimalFormat("#.##");
////        return df.format(add);
//        return add.toPlainString();
//    }

}
