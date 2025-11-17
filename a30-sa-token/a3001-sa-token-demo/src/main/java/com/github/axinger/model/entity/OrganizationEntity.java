package com.github.axinger.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrganizationEntity {

    /// 组织机构id
    private String id;

    /// 组织机构name
    private String name;

    /// 地区
    private String region;
}
