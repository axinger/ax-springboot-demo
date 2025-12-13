package com.github.axinger.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleDto {
    private Long userId;
    private String name;
    private String roleName;
    private String other;

    /**
     * age > 18
     */
    private Boolean isAdult;
}
