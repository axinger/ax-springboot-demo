package com.axing.common.vo;

import com.axing.common.response.dto.Result;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PageResult<T> extends Result<T> {

    private int pageNo;
    private int pageSize;
    private int total;
}
