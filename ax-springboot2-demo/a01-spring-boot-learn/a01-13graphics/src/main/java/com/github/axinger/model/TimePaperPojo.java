package com.github.axinger.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimePaperPojo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Builder.Default
    private String bestTime = "表头：";

    /// 表头,内容
    @Builder.Default
    private String bestTimeContent = "03-17(周日)08:45~03-19(周二)08:45";

    /// 可用时间
    @Builder.Default
    private String availableTimeTitle = "可用时间：";

    /// 可用时间,内容
    @Builder.Default
    private String availableTimeContent = "03-17(周日)08:45";

    @Builder.Default
    private String productName = "电视机";

    /// 制作区域
    @Builder.Default
    private String area = "生产线一";

    /// 制作说明
    @Builder.Default
    private String instructionsTitle = "【说明】：";

    /// 制作说明,内容
    @Builder.Default
    private String instructions = "备注说明123";

}
