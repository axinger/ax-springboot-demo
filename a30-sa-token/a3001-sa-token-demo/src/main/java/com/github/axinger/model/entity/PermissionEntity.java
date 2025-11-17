package com.github.axinger.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PermissionEntity {
    private String name;
    private String code;
}
