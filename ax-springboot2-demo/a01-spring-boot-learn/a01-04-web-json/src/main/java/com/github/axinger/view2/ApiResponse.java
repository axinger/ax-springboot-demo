package com.github.axinger.view2;

import com.fasterxml.jackson.annotation.JsonView;

@JsonView(Views.Basic.class)
public class ApiResponse<T> {
    private T data;

//    @JsonView(Views.Admin.class)
//    private AuditInfo audit;
}
