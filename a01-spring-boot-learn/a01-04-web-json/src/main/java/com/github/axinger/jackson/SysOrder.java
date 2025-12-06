package com.github.axinger.jackson;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

@Data
public class SysOrder {

    //序列化 User 时会包含 orders，但每个 Order 中的 user 字段不会被输出，避免循环。
    @JsonBackReference
    public SysUser user;
    private String id;
}
