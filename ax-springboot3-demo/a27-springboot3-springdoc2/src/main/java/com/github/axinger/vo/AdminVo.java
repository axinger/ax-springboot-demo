package com.github.axinger.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "管理员")
public class AdminVo {

    @Schema(description = "权限list")
    private List<String> portList;

}
