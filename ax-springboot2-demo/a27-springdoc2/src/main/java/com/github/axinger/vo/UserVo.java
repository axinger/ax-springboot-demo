package com.github.axinger.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户信息")
public class UserVo {

    @Schema(description = "用户名")
    private String name;

}
