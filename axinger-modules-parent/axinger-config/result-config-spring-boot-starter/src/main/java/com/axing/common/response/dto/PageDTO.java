package com.axing.common.response.dto;

import lombok.Data;

@Data
public class PageDTO<T> {
    private int pageNo;
    private int pageSize;
    private T data;
}
