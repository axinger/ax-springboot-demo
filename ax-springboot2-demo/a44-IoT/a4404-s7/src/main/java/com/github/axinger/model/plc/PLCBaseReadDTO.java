package com.github.axinger.model.plc;

import lombok.Data;

@Data
public class PLCBaseReadDTO<T> {
    private Integer dbNo;
    private T data;
}
